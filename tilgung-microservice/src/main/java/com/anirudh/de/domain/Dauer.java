package com.anirudh.de.domain;

import java.time.LocalDate;

public class Dauer {

  private LocalDate erstesDatum;
  private LocalDate letztesDatum;

  public Dauer(LocalDate erstesDatum, LocalDate letztesDatum) {
    this.erstesDatum = erstesDatum;
    this.letztesDatum = letztesDatum;
  }

  public Dauer() {
  }

  public LocalDate getErstesDatum() {
    return erstesDatum;
  }

  public void setErstesDatum(LocalDate erstesDatum) {
    this.erstesDatum = erstesDatum;
  }

  public LocalDate getLetztesDatum() {
    return letztesDatum;
  }

  public void setLetztesDatum(LocalDate letztesDatum) {
    this.letztesDatum = letztesDatum;
  }

  @Override
  public String toString() {
    return "Dauer{" +
      "erstesDatum=" + erstesDatum +
      ", letztesDatum=" + letztesDatum +
      '}';
  }
}
