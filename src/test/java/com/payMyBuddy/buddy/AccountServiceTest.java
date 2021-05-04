package com.payMyBuddy.buddy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.AccountRepository;
import com.payMyBuddy.buddy.service.AccountServiceImpl;
import com.payMyBuddy.buddy.service.UserBuddyServiceI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(AccountServiceImpl.class)
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
  
  @MockBean
  @Qualifier("userDetailsServiceImpl")
  private UserDetailsService userDetailsService;
  
  @MockBean
  AccountRepository accountRepository;
  
  @MockBean
  UserBuddyServiceI userBuddyServiceI;
  
  @Autowired
  AccountServiceImpl accountServiceImpl;
  
  
  Account account2 = new Account();

  @Test
  void test() {
    //ARRANGE
    Account account = new Account();
    UserBuddy user = new UserBuddy();
    user.setId(1L);
    user.setEmail("user@gmail.com");
    user.setPassword("user");
    
    account2.setAccountId(1L);
    account2.setBalance(new BigDecimal("0"));;
    account2.setUserBuddy(user);

    
   when(accountRepository.save(account)).thenReturn(account2);
    
    //ACT
    String result = accountServiceImpl.save(user);
    
    //Assert
    assertThat(account2.getUserBuddy()).isEqualTo(user);
    assertThat(result).isEqualToIgnoringCase("success");

   

  }

}
