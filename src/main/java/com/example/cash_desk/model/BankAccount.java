package com.example.cash_desk.model;

import java.util.List;
import java.util.Objects;

public class BankAccount {

  private String cashierName;
  private List<Cash> accounts;

  public BankAccount(String cashierName, List<Cash> accounts) {
    this.cashierName = cashierName;
    this.accounts = accounts;
  }

  public String getCashierName() {
    return cashierName;
  }

  public void setCashierName(String cashierName) {
    this.cashierName = cashierName;
  }

  public List<Cash> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<Cash> accounts) {
    this.accounts = accounts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankAccount that = (BankAccount) o;
    return Objects.equals(cashierName, that.cashierName) && Objects.equals(
        accounts, that.accounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cashierName, accounts);
  }
}
