package com.anirudh.de.domain.request;

import java.math.BigDecimal;

public class AnfrageDTO {

  private BigDecimal darlehensbetrag;
  private BigDecimal sollzins;
  private BigDecimal tilgungsatz;
  private Integer zinsbildung;

  public AnfrageDTO(BigDecimal darlehensbetrag, BigDecimal sollzins,
    BigDecimal tilgungsatz, Integer zinsbildung) {
    this.darlehensbetrag = darlehensbetrag;
    this.sollzins = sollzins;
    this.tilgungsatz = tilgungsatz;
    this.zinsbildung = zinsbildung;
  }

  public AnfrageDTO() {
  }

  public BigDecimal getDarlehensbetrag() {
    return darlehensbetrag;
  }

  public void setDarlehensbetrag(BigDecimal darlehensbetrag) {
    this.darlehensbetrag = darlehensbetrag;
  }

  public BigDecimal getSollzins() {
    return sollzins;
  }

  public void setSollzins(BigDecimal sollzins) {
    this.sollzins = sollzins;
  }

  public BigDecimal getTilgungsatz() {
    return tilgungsatz;
  }

  public void setTilgungsatz(BigDecimal tilgungsatz) {
    this.tilgungsatz = tilgungsatz;
  }

  public Integer getZinsbildung() {
    return zinsbildung;
  }

  public void setZinsbildung(Integer zinsbildung) {
    this.zinsbildung = zinsbildung;
  }

  @Override
  public String toString() {
    return "AnfrageDTO{" +
      "darlehensbetrag=" + darlehensbetrag +
      ", sollzins=" + sollzins +
      ", tilgungsatz=" + tilgungsatz +
      ", zinsbildung=" + zinsbildung +
      '}';
  }
}
