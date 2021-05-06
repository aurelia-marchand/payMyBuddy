package com.payMyBuddy.buddy.service;

import org.springframework.stereotype.Service;

import com.payMyBuddy.buddy.model.Transaction;

@Service
public class BankPaymentImpl implements BankPayment {

  @Override
  public boolean requestAuthorization(Transaction transactionPayment) {
    // TODO Real implementation admin bank details to V1 app
    // now return always true, here we send transfer betwenn bank account sender and
    // receiver, and admin bank for the fee
    return true;
  }

}
