package com.payMyBuddy.buddy.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.AccountRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountServiceImpl implements AccountServiceI{
  
  @Autowired
  AccountRepository accountRepository;

  @Override
  public Account findByUserAccountId(UserBuddy user) {
    return accountRepository.findByUserBuddy(user);
  }

  @Override
  public String save(UserBuddy user) {
    // Create an account when new user suscribe
    Account account = new Account();
    account.setBalance(new BigDecimal("0"));
    account.setUserBuddy(user);
    accountRepository.save(account);
    return "success";
  }
}
