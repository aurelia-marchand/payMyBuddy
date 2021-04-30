package com.payMyBuddy.buddy.service;

import com.payMyBuddy.buddy.dto.TransactionDto;
import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.Transaction;
import com.payMyBuddy.buddy.model.Type;

public interface TransactionServiceI {

  Iterable<Transaction> findAllBySenderId(Account account);

  String save(TransactionDto transactionDto);

  Iterable<Transaction> findAllBySenderIdAndType(Account account, Type userToUser);
}
