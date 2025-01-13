package com.example.cash_desk.service;

import com.example.cash_desk.dto.BankAccountDto;
import com.example.cash_desk.dto.CashDto;
import com.example.cash_desk.enums.Operations;
import com.example.cash_desk.repository.CashRepository;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashServiceImpl implements CashService {

  private final CashRepository repository;
  private final Path transactionHistoryFile = Paths.get("transaction_history.txt");
  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  @Override
  public String cashOperation(Operations operation, CashDto cashDto) {
    BankAccountDto account = repository.getAccount(cashDto.cashierName());
    if (account == null) {
      return "Cashier not found.";
    }

    var updatedAccounts = account.accounts().stream()
        .map(existingAccount -> existingAccount.currency().equals(cashDto.currency())
            ? performOperation(operation, existingAccount, cashDto)
            : existingAccount)
        .collect(Collectors.toList());

    repository.updateAccount(account.cashierName(), new BankAccountDto(account.cashierName(), updatedAccounts));

    logTransaction(operation, cashDto);
    return "Operation successful.";
  }

  private CashDto performOperation(Operations operation, CashDto current, CashDto cashDto) {
    int totalCents = (current.whole() * 100 + current.cents()) +
        (operation == Operations.DEPOSIT ? 1 : -1) * (cashDto.whole() * 100 + cashDto.cents());

    if (totalCents < 0) {
      throw new IllegalArgumentException("Insufficient funds.");
    }

    return new CashDto(
        current.currency(),
        totalCents / 100,
        (byte) (totalCents % 100),
        LocalDateTime.now(),
        current.cashierName());
  }

  private void logTransaction(Operations operation, CashDto cashDto) {
    String log = String.format("[%s] Operation: %s, Currency: %s, Amount: %d.%02d%n",
        DATE_TIME_FORMATTER.format(LocalDateTime.now()),
        operation.name(),
        cashDto.currency(),
        cashDto.whole(),
        cashDto.cents());

    try (BufferedWriter writer = Files.newBufferedWriter(
        transactionHistoryFile,
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND)) {
      writer.write(log);
    } catch (IOException e) {
      throw new RuntimeException("Failed to log transaction.", e);
    }
  }

  @Override
  public String cashBalance(String dateFrom, String dateTo, String cashierName) {
    LocalDateTime startDate = parseDate(dateFrom);
    LocalDateTime endDate = parseDate(dateTo);

    List<BankAccountDto> filteredAccounts = repository.getAllAccounts().stream()
        .filter(account -> (cashierName == null || account.cashierName().equalsIgnoreCase(cashierName)) &&
            account.accounts().stream().anyMatch(cash ->
                (startDate == null || !cash.lastTransactionDate().isBefore(startDate)) &&
                    (endDate == null || !cash.lastTransactionDate().isAfter(endDate))))
        .toList();

    if (filteredAccounts.isEmpty()) {
      return "No matching accounts found.";
    }

    StringBuilder accountInfo = new StringBuilder();
    for (BankAccountDto account : filteredAccounts) {
      accountInfo.append(account.toString()).append("\n");
    }
    return accountInfo.toString();
  }

  private LocalDateTime parseDate(String date) {
    if (date == null || date.isEmpty()) {
      return null;
    }
    try {
      return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid date format. Expected format: " + DATE_TIME_FORMATTER);
    }
  }
}