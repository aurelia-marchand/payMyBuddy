package com.payMyBuddy.buddy.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.AccountRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AccountServiceImpl implements AccountServiceI{
  
  @Autowired
  AccountRepository accountRepository;

  @Override
  public Account findByUserAccountId(UserBuddy user) {
    
    return accountRepository.findByUserBuddy(user);
  }

  @Override
  public Account save(UserBuddy user) {
    
    Account account = new Account();
    account.setBalance(new BigDecimal("0"));

    account.setUserBuddy(user);
    
    Account account2 = accountRepository.save(account);
    
    return account2;
  }

  

}
