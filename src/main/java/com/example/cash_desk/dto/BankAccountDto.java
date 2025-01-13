package com.example.cash_desk.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.time.LocalDateTime;

public record BankAccountDto(@NotEmpty String cashierName, @NotEmpty List<CashDto> accounts) {

  public LocalDateTime getLastTransactionDate() {
    return accounts.stream()
        .map(CashDto::lastTransactionDate)
        .max(LocalDateTime::compareTo)
        .orElse(LocalDateTime.now());
  }
}
