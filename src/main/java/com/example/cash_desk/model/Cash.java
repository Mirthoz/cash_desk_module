package com.example.cash_desk.model;

import com.example.cash_desk.enums.Currency;
import lombok.Getter;

@Getter
public class Cash {

  private final Currency currency;
  private int whole;
  private byte cents;

  public Cash(Currency currency, int whole, byte cents) {
    if (cents < 0 || cents >= 100) {
      throw new IllegalArgumentException("Cents must be between 0 and 99.");
    }
    this.currency = currency;
    this.whole = whole;
    this.cents = cents;
  }

  public void setWhole(int whole) {
    this.whole = whole;
  }

  public void setCents(byte cents) {
    if (cents < 0 || cents >= 100) {
      throw new IllegalArgumentException("Cents must be between 0 and 99.");
    }
    this.cents = cents;
  }

  public void add(Cash other) {
    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException("Currencies must match for addition.");
    }

    int totalCents = (this.whole * 100 + this.cents) +
        (other.whole * 100 + other.cents);

    this.whole = totalCents / 100;
    this.cents = (byte) (totalCents % 100);
  }

  public void subtract(Cash other) {
    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException("Currencies must match for subtraction.");
    }

    int totalCents = (this.whole * 100 + this.cents) -
        (other.whole * 100 + other.cents);

    if (totalCents < 0) {
      throw new IllegalArgumentException("Resulting amount cannot be negative.");
    }

    this.whole = totalCents / 100;
    this.cents = (byte) (totalCents % 100);
  }

  @Override
  public String toString() {
    return String.format("%s %d.%02d", currency, whole, cents);
  }
}

