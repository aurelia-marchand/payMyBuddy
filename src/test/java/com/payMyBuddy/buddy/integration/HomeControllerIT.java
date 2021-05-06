package com.payMyBuddy.buddy.integration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.payMyBuddy.buddy.dto.TransactionDto;
import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.AccountRepository;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
@TestPropertySource(locations = "classpath:application.properties")
@Sql("/insert-data.sql")
class HomeControllerIT {
  
  @Autowired
  private MockMvc mockMvc;
  
  @Autowired
  AccountRepository accountRepository;
  @Autowired
  UserBuddyRepository userBuddyRepository;

  @Test
  @Transactional
  @WithMockUser(username = "aur@gmail.com")
  void testUserHome() throws Exception {
    this.mockMvc.perform(get("/home")).andDo(print()).andExpect(status().isOk())
    .andExpect(content().string(containsString("home")));
  }
  
  @Test
  @Transactional
  @WithMockUser(username = "admin@paymybuddy.com")
  void testAdminHome() throws Exception {
    this.mockMvc.perform(get("/home")).andDo(print()).andExpect(status().isOk())
    .andExpect(content().string(containsString("admin")));
  }
  
  @Test
  @WithMockUser("aur@gmail.com")
  void testTransfer() throws Exception {
    // ARRANGE
    TransactionDto transac = new TransactionDto();
    UserBuddy user = userBuddyRepository.findByemail("aur@gmail.com");
    Account account = accountRepository.findByUserBuddy(user);
    
    transac.setAmount(new BigDecimal("100"));
    transac.setSenderId(account);
    transac.setDescription("payment");
    
    RequestBuilder request = post("/home")
        .param("description", transac.getDescription()).param("amount", transac.getAmount().toString());
    //ACT AND ASSERT
    mockMvc
        .perform(request)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(redirectedUrl("/home?successPayment"));
    
  }

}
