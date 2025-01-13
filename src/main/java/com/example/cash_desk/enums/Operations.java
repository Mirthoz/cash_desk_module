package com.example.cash_desk.enums;

import lombok.Getter;

@Getter
public enum Operations {
  DEPOSIT("Deposit"), WITHDRAWAL("Withdrawal");

  private final String displayName;

  Operations(String displayName) {
    this.displayName = displayName;
  }
}
