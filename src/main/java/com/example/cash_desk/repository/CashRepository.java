package com.example.cash_desk.repository;

import com.example.cash_desk.dto.BankAccountDto;
import com.example.cash_desk.dto.CashDto;
import com.example.cash_desk.enums.Currency;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class CashRepository {

  private final Map<String, BankAccountDto> accounts = new HashMap<>();
  private final Path transactionHistoryFile = Paths.get("transaction_history.txt");
  private final Path balanceFile = Paths.get("balances.txt");

  public CashRepository() {
    initializeData();
  }

  private void initializeData() {
    LocalDateTime now = LocalDateTime.now();

    List<CashDto> martinaAccounts = List.of(
        new CashDto(Currency.BGN, 1000, (byte) 0, now, "MARTINA"),
        new CashDto(Currency.EUR, 2000, (byte) 0, now, "MARTINA")
    );
    accounts.put("MARTINA", new BankAccountDto("MARTINA", martinaAccounts));

    List<CashDto> peterAccounts = List.of(
        new CashDto(Currency.BGN, 1000, (byte) 0, now, "PETER"),
        new CashDto(Currency.EUR, 2000, (byte) 0, now, "PETER")
    );
    accounts.put("PETER", new BankAccountDto("PETER", peterAccounts));

    List<CashDto> lindaAccounts = List.of(
        new CashDto(Currency.BGN, 1000, (byte) 0, now, "LINDA"),
        new CashDto(Currency.EUR, 2000, (byte) 0, now, "LINDA")
    );
    accounts.put("LINDA", new BankAccountDto("LINDA", lindaAccounts));
  }

  public BankAccountDto getAccount(String cashierName) {
    return accounts.get(cashierName);
  }

  public void updateAccount(String cashierName, BankAccountDto account) {
    accounts.put(cashierName, account);
  }

  public List<BankAccountDto> getAllAccounts() {
    return new ArrayList<>(accounts.values());
  }

  public List<String> getTransactions(String dateFrom, String dateTo, String cashierName) {
    List<String> filteredTransactions = new ArrayList<>();

    try {
      List<String> allTransactions = Files.readAllLines(transactionHistoryFile);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

      LocalDate startDate = (dateFrom != null) ? LocalDate.parse(dateFrom) : null;
      LocalDate endDate = (dateTo != null) ? LocalDate.parse(dateTo) : null;

      for (String transaction : allTransactions) {
        String[] parts = transaction.split(":");
        LocalDate transactionDate = LocalDate.parse(parts[0], formatter);
        String transactionCashierName = parts[1].split(" ")[0];

        if ((cashierName == null || transactionCashierName.equals(cashierName)) &&
            (startDate == null || !transactionDate.isBefore(startDate)) &&
            (endDate == null || !transactionDate.isAfter(endDate))) {
          filteredTransactions.add(transaction);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to read transaction history.", e);
    }

    return filteredTransactions;
  }

  public String getBalance(String cashierName) {
    BankAccountDto account = accounts.get(cashierName);
    if (account == null) {
      return "Cashier not found.";
    }
    return account.toString();
  }

  public void saveBalance(BankAccountDto account) {
    try {
      Files.writeString(balanceFile, account.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new RuntimeException("Failed to save balance.", e);
    }
  }
}
