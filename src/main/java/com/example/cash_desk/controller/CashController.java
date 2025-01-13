package com.example.cash_desk.controller;

import com.example.cash_desk.dto.CashDto;
import com.example.cash_desk.enums.Operations;
import com.example.cash_desk.service.CashService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class CashController {

  private final CashService cashService;

  private static final String AUTH_HEADER = "FIB-X-AUTH";

  @Value("${apiKey}")
  private String AUTH_KEY;

  @PostMapping("cash-operation")
  public ResponseEntity<String> cashOperation(
      @RequestHeader(value = AUTH_HEADER, required = true) String authKey,
      @RequestBody @Valid CashDto cashDto,
      @RequestParam Operations operation) {
    if (!AUTH_KEY.equals(authKey)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API key");
    }
    return ResponseEntity.ok(cashService.cashOperation(operation, cashDto));
  }

  @GetMapping("cash-balance")
  public ResponseEntity<String> cashBalance(
      @RequestHeader(value = AUTH_HEADER, required = true) String authKey,
      @RequestParam(required = false) String dateFrom,
      @RequestParam(required = false) String dateTo,
      @RequestParam(required = false) String cashierName) {
    if (!AUTH_KEY.equals(authKey)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API key");
    }

    String balanceInfo = cashService.cashBalance(dateFrom, dateTo, cashierName);

    return ResponseEntity.ok(balanceInfo);
  }
}