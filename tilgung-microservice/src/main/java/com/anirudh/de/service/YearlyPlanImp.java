package com.anirudh.de.service;

import com.anirudh.de.domain.exception.TilgungPlanCreationException;
import com.anirudh.de.domain.request.AnfrageDTO;
import com.anirudh.de.domain.response.MonatlicheBezahlungDTO;
import com.anirudh.de.domain.response.YearlyBezahlungDTO;
import com.anirudh.de.domain.response.YearlyPlanDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class YearlyPlanImp extends AbstractTilgungService<YearlyBezahlungDTO> {

  private static final Logger log = LogManager.getLogger(YearlyPlanImp.class);
  private static final BigDecimal zero = new BigDecimal(0.00);

  private MonatlichePlanImp monthlyService;

  public YearlyPlanImp(MonatlichePlanImp monthlyService) {
    this.monthlyService = monthlyService;
  }

  @Override
  public Optional<List<YearlyBezahlungDTO>> planErzeugen(AnfrageDTO anfrageDTO) throws TilgungPlanCreationException {
    verifyRequestPayload(anfrageDTO);

    BigDecimal darlehnen = anfrageDTO.getDarlehensbetrag();
    List<YearlyBezahlungDTO> yearly = new ArrayList<>();

    Optional<List<MonatlicheBezahlungDTO>> monthlyPlanOpt = monthlyService.planErzeugen(anfrageDTO);

    if (!monthlyPlanOpt.isPresent() || monthlyPlanOpt.get().isEmpty()) {
      throw new TilgungPlanCreationException("Plan könnte nicht erzeugt werden.");
    }

    //Initiale Werte erzeugen für die For Schleife weiter unten
    List<MonatlicheBezahlungDTO> monthlyPlan = monthlyPlanOpt.get();
    int firstYear = monthlyPlan.get(0).getDatum().getYear();
    YearlyBezahlungDTO yearlyCounter = new YearlyBezahlungDTO();
    yearlyCounter.setYear(Integer.toString(firstYear));
    yearlyCounter.setRestSchuld(darlehnen);

    //Für jedes jahr die Bezahlung Anteilen im Summe berechnen
    for (int i = 0; i < monthlyPlan.size(); i++) {
      MonatlicheBezahlungDTO monthly = monthlyPlan.get(i);

      if (monthly.getDatum().getYear() != Integer.parseInt(yearlyCounter.getYear())) {
        yearly.add(yearlyCounter);

        //Mit dem letzten Jahr erledigt, jetzt counter resetten!
        yearlyCounter = new YearlyBezahlungDTO();
        yearlyCounter.setYear(Integer.valueOf(monthly.getDatum().getYear()).toString());
        yearlyCounter.setRestSchuld(monthly.getRestSchuld());
      }

      final BigDecimal currentRate = yearlyCounter.getRate() == null ? zero : yearlyCounter.getRate();
      final BigDecimal currentZinsen = yearlyCounter.getZinsen() == null ? zero : yearlyCounter.getZinsen();
      final BigDecimal currentTilgung = yearlyCounter.getTilgung() == null ? zero : yearlyCounter.getTilgung();

      //Die Jährliche Anteile für ein bestimmtes Jahr weiter inkrementieren.
      yearlyCounter.setRate(currentRate.add(monthly.getRate()));
      yearlyCounter.setZinsen(currentZinsen.add(monthly.getZinsen()));
      yearlyCounter.setTilgung(currentTilgung.add(monthly.getTilgung()));
      yearlyCounter.setRestSchuld(monthly.getRestSchuld());

      //Falls die letzte Rate nicht ganz im Dezember erfolgt dann die relevante Dauer im Jahr erfassen.
      if (i == monthlyPlan.size() - 1) {
        if (monthly.getDatum().getMonthValue() != 12) {
          yearlyCounter.setYear("JAN - " + monthly.getDatum().getMonth() + " " + monthly.getDatum().getYear());
          yearly.add(yearlyCounter);
        } else {
          yearlyCounter.setYear(Integer.valueOf(monthly.getDatum().getYear()).toString());
          yearly.add(yearlyCounter);
        }
      }
    }

    return Optional.of(yearly);
  }

  public Optional<YearlyPlanDTO> jahrlicherPlanMitMonatsRateErzeugen(AnfrageDTO anfrageDTO)
    throws TilgungPlanCreationException {
    verifyRequestPayload(anfrageDTO);
    YearlyPlanDTO yearlyPlanDTO = new YearlyPlanDTO();
    final Optional<List<YearlyBezahlungDTO>> yearlyBezahlungDTOOpt = this.planErzeugen(anfrageDTO);

    final BigDecimal darlehensbetrag = anfrageDTO.getDarlehensbetrag();
    final BigDecimal sollzins = anfrageDTO.getSollzins();
    final BigDecimal tilgungsatz = anfrageDTO.getTilgungsatz();

    if (!yearlyBezahlungDTOOpt.isPresent() || yearlyBezahlungDTOOpt.get().isEmpty()) {
      throw new TilgungPlanCreationException("Plan könnte nicht erzeugt werden.");
    }
    List<YearlyBezahlungDTO> yearlyList = yearlyBezahlungDTOOpt.get();

    yearlyPlanDTO.setYearlyBezahlungDTOS(yearlyBezahlungDTOOpt.get());

    //Die montaliche Rate und Restschuld fürs Jährliche DTO Objekt setzetn
    yearlyPlanDTO.setMonthlyRate(monthlyService.calculateMonthlyRate(darlehensbetrag, sollzins, tilgungsatz));
    yearlyPlanDTO.setRestSchuld(yearlyList.get(yearlyList.size() - 1).getRestSchuld());

    return Optional.of(yearlyPlanDTO);
  }
}
