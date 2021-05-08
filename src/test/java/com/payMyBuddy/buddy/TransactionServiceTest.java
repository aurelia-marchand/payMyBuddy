package com.payMyBuddy.buddy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.payMyBuddy.buddy.dto.TransactionDto;
import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.Transaction;
import com.payMyBuddy.buddy.model.Type;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.AccountRepository;
import com.payMyBuddy.buddy.repository.TransactionRepository;
import com.payMyBuddy.buddy.service.AccountServiceI;
import com.payMyBuddy.buddy.service.BankPayment;
import com.payMyBuddy.buddy.service.TransactionServiceImpl;
import com.payMyBuddy.buddy.service.UserBuddyServiceI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(TransactionServiceImpl.class)
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
  
  @MockBean
  TransactionRepository transactionRepository;
  
  @MockBean
  @Qualifier("userDetailsServiceImpl")
  private UserDetailsService userDetailsService;

  @MockBean
  UserBuddyServiceI userBuddyServiceI;

  @MockBean
  AccountServiceI accountServiceI;

  @MockBean
  BankPayment bankPayment;

  @MockBean
  AccountRepository accountRepository;
  
  @MockBean
  Authentication authentification;
  
  @Autowired
  TransactionServiceImpl transactionServiceImpl;

  @Test
  void testTransactionUserToUserInSuccessAccountBalanceAndFeeOk() {
    //ARRANGE
    Account account = new Account();
    account.setBalance(new BigDecimal("105"));
    account.setAccountId(1);
    UserBuddy user = new UserBuddy();
    user.setEmail("user1@gmail.com");
    account.setUserBuddy(user);
    
    TransactionDto transac = new TransactionDto();
    transac.setAmount(new BigDecimal("100"));
    transac.setType(Type.USER_TO_USER);
    transac.setDescription("remboursement");
    transac.setSenderId(account);
    transac.setMailBeneficiary("user2@gmail.com");
    
    UserBuddy userB = new UserBuddy();
    userB.setEmail("user2@gmail.com");
    
    Account accountB = new Account();
    accountB.setBalance(new BigDecimal("100"));

    when(userBuddyServiceI.findOne("user1@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    when(userBuddyServiceI.findOne("user2@gmail.com")).thenReturn(userB);
    when(accountServiceI.findByUserAccountId(userB)).thenReturn(accountB);
    when(bankPayment.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
    when(accountRepository.getOne(account.getAccountId())).thenReturn(account);
    when(accountRepository.getOne(accountB.getAccountId())).thenReturn(accountB);

    //ACT
    String reponse = transactionServiceImpl.save(transac);
    
    //ASSERT
    assertThat(reponse).isEqualToIgnoringCase("success");
    assertThat(accountB.getBalance()).isEqualTo(new BigDecimal("200"));
    assertThat(account.getBalance()).isEqualTo(new BigDecimal("4.50"));
  }
  
  @Test
  void testTransactionUserToUserNotEnoughMoney() {
    //ARRANGE
    Account account = new Account();
    account.setBalance(new BigDecimal("100"));
    account.setAccountId(1);
    UserBuddy user = new UserBuddy();
    user.setEmail("user1@gmail.com");
    account.setUserBuddy(user);
    
    TransactionDto transac = new TransactionDto();
    transac.setAmount(new BigDecimal("100"));
    transac.setType(Type.USER_TO_USER);
    transac.setDescription("remboursement");
    transac.setSenderId(account);
    transac.setMailBeneficiary("user2@gmail.com");
    
    UserBuddy userB = new UserBuddy();
    userB.setEmail("user2@gmail.com");
    
    Account accountB = new Account();
    accountB.setBalance(new BigDecimal("100"));
    
    when(userBuddyServiceI.findOne("user1@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    when(userBuddyServiceI.findOne("user2@gmail.com")).thenReturn(userB);
    when(accountServiceI.findByUserAccountId(userB)).thenReturn(accountB);
    when(bankPayment.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
    when(accountRepository.getOne(account.getAccountId())).thenReturn(account);
    when(accountRepository.getOne(accountB.getAccountId())).thenReturn(accountB);

    //ACT
    String reponse = transactionServiceImpl.save(transac);
    
    //ASSERT
    assertThat(reponse).isEqualToIgnoringCase("errorNotEnoughMoney");
    assertThat(account.getBalance()).isEqualTo(new BigDecimal("100"));
  }
  
  @Test
  void testTransactionBankPaymentBalanceUpdateOk() {
    //ARRANGE
    Account account = new Account();
    account.setBalance(new BigDecimal("100"));
    account.setAccountId(1);
    UserBuddy user = new UserBuddy();
    user.setEmail("user1@gmail.com");
    account.setUserBuddy(user);
    
    TransactionDto transac = new TransactionDto();
    transac.setAmount(new BigDecimal("100"));
    transac.setDescription("payment");
    transac.setSenderId(account);
    
    
    when(userBuddyServiceI.findOne("user1@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    when(bankPayment.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
    when(accountRepository.getOne(account.getAccountId())).thenReturn(account);
    
    //ACT
    String reponse = transactionServiceImpl.save(transac);
    
    //ASSERT
    assertThat(reponse).isEqualToIgnoringCase("success");
    assertThat(account.getBalance()).isEqualTo(new BigDecimal("200"));
    
  }
  
  @Test
  void testTransactionBankWithraw() {
    //ARRANGE
    Account account = new Account();
    account.setBalance(new BigDecimal("100"));
    account.setAccountId(1);
    UserBuddy user = new UserBuddy();
    user.setEmail("user1@gmail.com");
    account.setUserBuddy(user);
    
    TransactionDto transac = new TransactionDto();
    transac.setAmount(new BigDecimal("100"));
    transac.setDescription("withdraw");
    transac.setSenderId(account);
    
    
    when(userBuddyServiceI.findOne("user1@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    when(bankPayment.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
    when(accountRepository.getOne(account.getAccountId())).thenReturn(account);
    
    //ACT
    String reponse = transactionServiceImpl.save(transac);
    
    //ASSERT
    assertThat(reponse).isEqualToIgnoringCase("success");
    assertThat(account.getBalance()).isEqualTo(new BigDecimal("0"));
    
  }
  
  @Test
  void testTransactionBankWithrawNotEnoughMoney() {
    //ARRANGE
    Account account = new Account();
    account.setBalance(new BigDecimal("100"));
    account.setAccountId(1);
    UserBuddy user = new UserBuddy();
    user.setEmail("user1@gmail.com");
    account.setUserBuddy(user);
    
    TransactionDto transac = new TransactionDto();
    transac.setAmount(new BigDecimal("105"));
    transac.setDescription("withdraw");
    transac.setSenderId(account);
    
    
    when(userBuddyServiceI.findOne("user1@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    when(bankPayment.requestAuthorization(Mockito.any(Transaction.class))).thenReturn(true);
    when(accountRepository.getOne(account.getAccountId())).thenReturn(account);
    
    //ACT
    String reponse = transactionServiceImpl.save(transac);
    
    //ASSERT
    assertThat(reponse).isEqualToIgnoringCase("errorNotEnoughMoney");
    assertThat(account.getBalance()).isEqualTo(new BigDecimal("100"));
    
  }

}
