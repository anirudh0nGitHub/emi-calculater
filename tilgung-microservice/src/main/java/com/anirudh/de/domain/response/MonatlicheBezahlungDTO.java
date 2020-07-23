package com.anirudh.de.domain.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class MonatlicheBezahlungDTO extends AbstractTilgungResponse {

  private LocalDate datum;
  private BigDecimal restSchuld;
  private BigDecimal zinsen;
  private BigDecimal tilgung;
  private BigDecimal rate;

  public MonatlicheBezahlungDTO() {
  }

  public MonatlicheBezahlungDTO(LocalDate datum, BigDecimal restSchuld, BigDecimal zinsen, BigDecimal tilgung,
    BigDecimal rate) {
    this.datum = datum;
    this.restSchuld = restSchuld;
    this.zinsen = zinsen;
    this.tilgung = tilgung;
    this.rate = rate;
  }

  public BigDecimal getRestSchuld() {
    return restSchuld;
  }

  public void setRestSchuld(BigDecimal restSchuld) {
    this.restSchuld = restSchuld;
  }

  public BigDecimal getTilgung() {
    return tilgung;
  }

  public void setTilgung(BigDecimal tilgung) {
    this.tilgung = tilgung;
  }

  public BigDecimal getZinsen() {
    return zinsen;
  }

  public void setZinsen(BigDecimal zinsen) {
    this.zinsen = zinsen;
  }

  public LocalDate getDatum() {
    return datum;
  }

  public void setDatum(LocalDate datum) {
    this.datum = datum;
  }

  public BigDecimal getRate() {
    return rate;
  }

  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }

  @Override
  public String toString() {
    return "MonatlicheBezahlungDTO{" +
      "datum=" + datum +
      ", restSchuld=" + restSchuld +
      ", zinsen=" + zinsen +
      ", tilgung=" + tilgung +
      ", rate=" + rate +
      '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(datum, restSchuld, zinsen, tilgung, rate);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MonatlicheBezahlungDTO that = (MonatlicheBezahlungDTO) o;
    return Objects.equals(datum, that.datum) &&
      Objects.equals(restSchuld, that.restSchuld) &&
      Objects.equals(zinsen, that.zinsen) &&
      Objects.equals(tilgung, that.tilgung) &&
      Objects.equals(rate, that.rate);
  }
}
