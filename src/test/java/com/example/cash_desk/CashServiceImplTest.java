package com.example.cash_desk;

import com.example.cash_desk.dto.CashDto;
import com.example.cash_desk.dto.BankAccountDto;
import com.example.cash_desk.enums.Currency;
import com.example.cash_desk.enums.Operations;
import com.example.cash_desk.repository.CashRepository;
import com.example.cash_desk.service.CashServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CashServiceImplTest {

  @Mock
  private CashRepository repository;

  @InjectMocks
  private CashServiceImpl cashService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void cashOperation_ShouldReturnCashierNotFoundForInvalidAccount() {
    // Arrange
    CashDto deposit = new CashDto(Currency.BGN, 600, (byte) 0, LocalDateTime.now(), "MARTINA");

    when(repository.getAccount("MARTINA")).thenReturn(null);

    // Act
    String response = cashService.cashOperation(Operations.DEPOSIT, deposit);

    // Assert
    assertEquals("Cashier not found.", response);
  }

  @Test
  void cashBalance_ShouldReturnNoMatchingAccounts() {
    // Arrange
    when(repository.getAllAccounts()).thenReturn(List.of());

    // Act
    String response = cashService.cashBalance(null, null, "MARINA");

    // Assert
    assertEquals("No matching accounts found.", response);
  }

  @Test
  void cashBalance_ShouldReturnFilteredAccounts() {
    // Arrange
    CashDto account1Balance = new CashDto(Currency.BGN, 500, (byte) 0, LocalDateTime.now(), "MARTINA");
    CashDto account2Balance = new CashDto(Currency.BGN, 1000, (byte) 0, LocalDateTime.now(), "JOHN");
    BankAccountDto account1 = new BankAccountDto("MARTINA", List.of(account1Balance));
    BankAccountDto account2 = new BankAccountDto("JOHN", List.of(account2Balance));

    when(repository.getAllAccounts()).thenReturn(List.of(account1, account2));

    // Act
    String response = cashService.cashBalance(null, null, "MARTINA");

    // Assert
    assertTrue(response.contains("MARTINA"));
    assertFalse(response.contains("JOHN"));
  }
}
