package com.example.cash_desk.service;

import com.example.cash_desk.dto.CashDto;
import com.example.cash_desk.enums.Operations;

public interface CashService {

  String cashOperation(Operations operation, CashDto cashDto);

  String cashBalance(String dateFrom, String dateTo, String cashierName);
}
