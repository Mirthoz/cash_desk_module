package com.example.cash_desk.dto;

import com.example.cash_desk.enums.Currency;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CashDto(
    @NotNull Currency currency,
    int whole,
    byte cents,
    LocalDateTime lastTransactionDate,
    @NotNull String cashierName) {

  public CashDto {
    if (cents < 0 || cents >= 100) {
      throw new IllegalArgumentException("Cents must be between 0 and 99.");
    }
  }
}

