package com.anirudh.de.domain.response;

import java.math.BigDecimal;
import java.util.List;

public class YearlyPlanDTO extends AbstractTilgungResponse {

  private BigDecimal monthlyRate;
  private BigDecimal restSchuld;
  private List<YearlyBezahlungDTO> yearlyBezahlungDTOS;

  public YearlyPlanDTO() {
  }

  public YearlyPlanDTO(BigDecimal monthlyRate, BigDecimal restSchuld,
    List<YearlyBezahlungDTO> yearlyBezahlungDTOS) {
    this.monthlyRate = monthlyRate;
    this.restSchuld = restSchuld;
    this.yearlyBezahlungDTOS = yearlyBezahlungDTOS;
  }

  @Override
  public String toString() {
    return "YearlyPlanDTO{" +
      "monthlyRate=" + monthlyRate +
      ", restSchuld=" + restSchuld +
      ", yearlyBezahlungDTOS=" + yearlyBezahlungDTOS.toString() +
      '}';
  }

  public BigDecimal getMonthlyRate() {
    return monthlyRate;
  }

  public void setMonthlyRate(BigDecimal monthlyRate) {
    this.monthlyRate = monthlyRate;
  }

  public BigDecimal getRestSchuld() {
    return restSchuld;
  }

  public void setRestSchuld(BigDecimal restSchuld) {
    this.restSchuld = restSchuld;
  }

  public List<YearlyBezahlungDTO> getYearlyBezahlungDTOS() {
    return yearlyBezahlungDTOS;
  }

  public void setYearlyBezahlungDTOS(
    List<YearlyBezahlungDTO> yearlyBezahlungDTOS) {
    this.yearlyBezahlungDTOS = yearlyBezahlungDTOS;
  }
}
