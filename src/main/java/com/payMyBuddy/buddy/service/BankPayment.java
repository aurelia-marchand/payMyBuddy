package com.payMyBuddy.buddy.service;

import com.payMyBuddy.buddy.model.Transaction;

/**
 * Inteface for prototype facturation, have to implement real facturation later
 * @author aurel
 *
 */
public interface BankPayment {

  boolean requestAuthorization(Transaction transactionPayment);

}
