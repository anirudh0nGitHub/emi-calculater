package com.anirudh.de.domain.response;

import java.math.BigDecimal;
import java.util.Objects;

public class YearlyBezahlungDTO extends AbstractTilgungResponse {

    private String year;
    private BigDecimal rate;
    private BigDecimal zinsen;
    private BigDecimal tilgung;
    private BigDecimal restSchuld;

    public YearlyBezahlungDTO(String year, BigDecimal rate, BigDecimal zinsen, BigDecimal tilgung,
                              BigDecimal restSchuld) {
        this.year = year;
        this.rate = rate;
        this.zinsen = zinsen;
        this.tilgung = tilgung;
        this.restSchuld = restSchuld;
    }

    public YearlyBezahlungDTO() {
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getZinsen() {
        return zinsen;
    }

    public void setZinsen(BigDecimal zinsen) {
        this.zinsen = zinsen;
    }

    public BigDecimal getTilgung() {
        return tilgung;
    }

    public void setTilgung(BigDecimal tilgung) {
        this.tilgung = tilgung;
    }

    public BigDecimal getRestSchuld() {
        return restSchuld;
    }

    public void setRestSchuld(BigDecimal restSchuld) {
        this.restSchuld = restSchuld;
    }

    @Override
    public String toString() {
        return "YearlyBezahlungDTO{" +
                "year=" + year +
                ", rate=" + rate +
                ", zinsen=" + zinsen +
                ", tilgung=" + tilgung +
                ", restSchuld=" + restSchuld +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YearlyBezahlungDTO that = (YearlyBezahlungDTO) o;
        return Objects.equals(year, that.year) &&
                Objects.equals(rate, that.rate) &&
                Objects.equals(zinsen, that.zinsen) &&
                Objects.equals(tilgung, that.tilgung) &&
                Objects.equals(restSchuld, that.restSchuld);
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, rate, zinsen, tilgung, restSchuld);
    }
}
