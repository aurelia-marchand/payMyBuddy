package com.payMyBuddy.buddy.service;

import org.springframework.stereotype.Service;

import com.payMyBuddy.buddy.model.Transaction;

@Service
public class BankPaymentImpl implements BankPayment{

  @Override
  public boolean requestAuthorization(Transaction transactionPayment) {
    // TODO Real implementation admin bank details
    return true;
  }

}
