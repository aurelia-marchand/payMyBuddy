package com.payMyBuddy.buddy.integration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.Transaction;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.AccountRepository;
import com.payMyBuddy.buddy.repository.TransactionRepository;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@Sql("/insert-data.sql")
class TransferControllerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  TransactionRepository transactionRepository;
  @Autowired
  UserBuddyRepository userBuddyRepository;
  @Autowired
  AccountRepository accountRepository;

  @Test
  @Transactional
  @WithMockUser(username = "aur@gmail.com")
  void testShouldReturnDefaultMessage() throws Exception {
    //ARRANGE
    UserBuddy user = userBuddyRepository.findByemail("aur@gmail.com");
    Account account = accountRepository.findByUserBuddy(user);
    Iterable<Transaction> listTransaction = transactionRepository.findAllBySenderId(account);
    Set<UserBuddy> contacts = user.getContacts();
 
    // ACT AND ASSERT
    this.mockMvc.perform(MockMvcRequestBuilders.get("/transfer").flashAttr("user", user)
        .flashAttr("transactions", listTransaction).flashAttr("contacts", contacts))
        .andExpect(status().isOk()).andExpect(content().string(containsString("transfer")));

  
  }

}
