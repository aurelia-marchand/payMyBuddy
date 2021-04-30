package com.payMyBuddy.buddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.Transaction;
import com.payMyBuddy.buddy.model.Type;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  Iterable<Transaction> findAllBySenderId(Account account);

  Iterable<Transaction> findAllBySenderIdAndType(Account account, Type userToUser);

}
