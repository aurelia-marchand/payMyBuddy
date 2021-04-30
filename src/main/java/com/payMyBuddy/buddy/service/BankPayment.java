package com.payMyBuddy.buddy.service;

import com.payMyBuddy.buddy.model.Transaction;

public interface BankPayment {

  boolean requestAuthorization(Transaction transactionPayment);

}
