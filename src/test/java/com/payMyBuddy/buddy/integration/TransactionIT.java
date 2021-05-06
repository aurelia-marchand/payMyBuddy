package com.payMyBuddy.buddy.integration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.Transaction;
import com.payMyBuddy.buddy.model.Type;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.AccountRepository;
import com.payMyBuddy.buddy.repository.TransactionRepository;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;
import com.payMyBuddy.buddy.service.TransactionServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@Sql("/insert-data.sql")
class TransactionIT {
  
  @Autowired
  TransactionServiceImpl transactionServiceImpl;
  
  @Autowired
  TransactionRepository transactionRpository;
  
  @Autowired
  AccountRepository accountRepository;
  
  @Autowired
  UserBuddyRepository userBuddyRepository;

  @Test
  @Transactional
  @WithMockUser(username = "aur@gmail.com")
  void testFindAllTransfer() {
    //ARRANGE
    UserBuddy user = userBuddyRepository.findByemail("aur@gmail.com");
    Account account = accountRepository.findByUserBuddy(user);
    
    //ACT
    Iterable<Transaction> transactions = transactionServiceImpl.findAllBySenderIdAndType(account, Type.USER_TO_USER);
   
    //ASSERT
    assertThat(transactions.toString()).contains("remboursement cadeau");
  }

}
