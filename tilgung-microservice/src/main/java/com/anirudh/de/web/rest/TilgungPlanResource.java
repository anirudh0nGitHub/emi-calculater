package com.anirudh.de.web.rest;

import com.anirudh.de.domain.exception.TilgungPlanCreationException;
import com.anirudh.de.domain.request.AnfrageDTO;
import com.anirudh.de.domain.response.MonatlicheBezahlungDTO;
import com.anirudh.de.domain.response.YearlyPlanDTO;
import com.anirudh.de.service.MonatlichePlanImp;
import com.anirudh.de.service.YearlyPlanImp;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller für die erstellung des Tilgungsplan.
 *
 * @author anirudh
 */
@RestController
@RequestMapping("/api/plan")
public class TilgungPlanResource {

  private static final Logger log = LogManager.getLogger(TilgungPlanResource.class);
  private YearlyPlanImp yearlyPlanImp;
  private MonatlichePlanImp monatlichePlanImp;


  public TilgungPlanResource(YearlyPlanImp yearlyPlanImp,
    MonatlichePlanImp monatlichePlanImp) {
    this.yearlyPlanImp = yearlyPlanImp;
    this.monatlichePlanImp = monatlichePlanImp;
  }

  /**
   * REST Endpunkt für die Erstellung Jährlicher aufgegliederter Plan
   *
   * @param request AnfrageDTO Data Transfer Object
   * @return Der durch die Angaben  des Darlehensbetrages, • des Sollzinses (in Prozent), • der anfänglichen Tilgung (in
   * Prozent) und • der Zinsbindung (in Jahren) erzeugte Tilgungsplan
   * @throws TilgungPlanCreationException Exception for the case when creation fails
   */
  @PostMapping("/yearly")
  public ResponseEntity<YearlyPlanDTO> jahrlicherPlanErstellen(@Valid @RequestBody AnfrageDTO request)
    throws TilgungPlanCreationException {
    final Optional<YearlyPlanDTO> plan = yearlyPlanImp.jahrlicherPlanMitMonatsRateErzeugen(request);

    if (!plan.isPresent()) {
      log.info("  Der Jährliche Plan könnte nicht erzeugt werden.");
      throw new TilgungPlanCreationException("Unable to create plan. Please try again later.");
    }
    log.info("serving request");
    return new ResponseEntity<>(plan.get(), HttpStatus.CREATED);
  }

  /**
   * REST Endpunkt für die Erstellung Monatlich aufgegliederter Plan
   *
   * @param request AnfrageDTO Data Transfer Object
   * @return Der durch die Angaben  des Darlehensbetrages, • des Sollzinses (in Prozent), • der anfänglichen Tilgung (in
   * Prozent) und • der Zinsbindung (in Jahren) erzeugte Tilgungsplan
   * @throws TilgungPlanCreationException Exception for the case when creation fails
   */
  @PostMapping("/monthly")
  public ResponseEntity<List<MonatlicheBezahlungDTO>> monatlicherPlanErstellen(@Valid @RequestBody AnfrageDTO request)
    throws TilgungPlanCreationException {
    final Optional<List<MonatlicheBezahlungDTO>> plan = monatlichePlanImp.planErzeugen(request);

    if (!plan.isPresent()) {
      log.info("Der Monatliche Plan könnte nicht erzeugt werden.");
      throw new TilgungPlanCreationException("Unable to create plan. Please try again later.");
    }
    log.info("serving request");
    return new ResponseEntity<>(plan.get(), HttpStatus.CREATED);
  }
}
