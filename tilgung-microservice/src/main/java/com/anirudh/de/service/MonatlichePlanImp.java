package com.anirudh.de.service;

import com.anirudh.de.domain.Dauer;
import com.anirudh.de.domain.exception.TilgungPlanCreationException;
import com.anirudh.de.domain.request.AnfrageDTO;
import com.anirudh.de.domain.response.MonatlicheBezahlungDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MonatlichePlanImp extends AbstractTilgungService<MonatlicheBezahlungDTO> {

  private static final Logger log = LogManager.getLogger(MonatlichePlanImp.class);
  private static final BigDecimal zero = new BigDecimal(0.00);

  @Override
  public Optional<List<MonatlicheBezahlungDTO>> planErzeugen(AnfrageDTO anfrageDTO)
    throws TilgungPlanCreationException {
    verifyRequestPayload(anfrageDTO);

    BigDecimal darlehnen = anfrageDTO.getDarlehensbetrag();
    final BigDecimal sollzins = anfrageDTO.getSollzins();
    final BigDecimal tilgungsatz = anfrageDTO.getTilgungsatz();
    Integer sinzbindung = anfrageDTO.getZinsbildung();

    return montalicherPlan(darlehnen, sollzins, tilgungsatz, sinzbindung);
  }

  private Optional<List<MonatlicheBezahlungDTO>> montalicherPlan(final BigDecimal darlehnen, BigDecimal sollzins,
    BigDecimal tilgungsatz, Integer sinzbindung) {

    List<MonatlicheBezahlungDTO> plan = new ArrayList<>();
    Predicate<BigDecimal> nichtVolltilgung = shuld -> shuld.compareTo(zero) > 0;
    BiPredicate<LocalDate, LocalDate> nichtSinzbindungEnde = LocalDate::isBefore;

    //Falls keine sinsbindung angegeben würde, dann wird ein Plan maximal bis Ende 50 Jahren gerechnet
    Dauer dauer = initDauer(sinzbindung);
    LocalDate currrent = dauer.getErstesDatum();

    BigDecimal rate = calculateMonthlyRate(darlehnen, sollzins, tilgungsatz);
    BigDecimal restSchuld = darlehnen;

    while (nichtVolltilgung.test(restSchuld) && nichtSinzbindungEnde.test(currrent, dauer.getLetztesDatum())) {
      MonatlicheBezahlungDTO dto = new MonatlicheBezahlungDTO();
      dto.setDatum(currrent);
      dto.setRate(rate);
      dto.setRestSchuld(restSchuld);

      //Für den ersten Monat die initiale Werte setzen, die erste Tilgung erfolt am Ende nächstes Monats.
      //z.B. Wenn man am 15ten Dezember 2020 den Plan erzeugt, dann wird hier davon ausgegangen, dass die erste Rate
      //wird am 31ten Jan 2021 bezahlt.
      if (currrent.isEqual(dauer.getErstesDatum())) {
        initialeWerte(restSchuld, rate, dto);
        plan.add(dto);

        currrent = currrent.plusMonths(1L).with(TemporalAdjusters.lastDayOfMonth());
        continue;
      }

      //Wenn nicht initial, dann fortlaufende Tilgung von den Tilgunganteilen und dem Restschuld ermitteln
      monatlicheAnteileAktualisieren(dto, sollzins, rate);

      //Falls nicht vollgetilgt, nach monatlicher Tilgung, darlehen/restshuld neu berechnen
      if (restSchuld.compareTo(zero) != 0) {
        restSchuld = nachMonatlicherTilgung(restSchuld, dto);
      }

      plan.add(dto);
      currrent = currrent.plusMonths(1L).with(TemporalAdjusters.lastDayOfMonth());
    }

    return Optional.of(plan);
  }

  BigDecimal calculateMonthlyRate(BigDecimal darlehnen, BigDecimal sollzins, BigDecimal tilgungsatz) {
    return darlehnen.multiply(sollzins.add(tilgungsatz))
      .divide(new BigDecimal(1200), 2, RoundingMode.HALF_UP);
  }

  private void initialeWerte(BigDecimal darlehnen, BigDecimal rate, MonatlicheBezahlungDTO dto) {
    dto.setRestSchuld(darlehnen);
    dto.setTilgung(zero);
    dto.setZinsen(zero);
    dto.setRate(rate);
  }

  private BigDecimal nachMonatlicherTilgung(BigDecimal darlehnen, MonatlicheBezahlungDTO dto) {
    dto.setRestSchuld(darlehnen.subtract(dto.getTilgung()));

    darlehnen = dto.getRestSchuld();
    return darlehnen;
  }

  private void monatlicheAnteileAktualisieren(MonatlicheBezahlungDTO dto, BigDecimal sollzins, BigDecimal rate) {

    BigDecimal restSchuld = dto.getRestSchuld();

    dto.setZinsen(restSchuld.multiply(sollzins).divide(new BigDecimal(1200), 2, RoundingMode.HALF_UP));

    //Im Falle einer möglichen Volltilgung, die Anteile dementsprechend berechnen
    if (dto.getRestSchuld().add(dto.getZinsen()).compareTo(rate) < 0) {
      dto.setRate(dto.getRestSchuld().add(dto.getZinsen()));
      dto.setTilgung(dto.getRestSchuld());
      dto.setRestSchuld(zero);
    } else {
      dto.setTilgung(rate.subtract(dto.getZinsen()));
    }
  }

  private Dauer initDauer(Integer sinzbindung) {
    Dauer dauer = new Dauer();
    dauer.setErstesDatum(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
    if (sinzbindung == null) {
      dauer.setLetztesDatum(dauer.getErstesDatum().plusYears(50L));
    } else {
      dauer.setLetztesDatum(dauer.getErstesDatum().plusMonths(1L).plusYears(sinzbindung.longValue()));
    }

    return dauer;
  }
}
