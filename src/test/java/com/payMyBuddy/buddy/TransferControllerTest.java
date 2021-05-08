package com.payMyBuddy.buddy;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.payMyBuddy.buddy.configuration.SecurityConfig;
import com.payMyBuddy.buddy.controller.TransferController;
import com.payMyBuddy.buddy.dto.TransactionDto;
import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.Transaction;
import com.payMyBuddy.buddy.model.Type;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.service.AccountServiceI;
import com.payMyBuddy.buddy.service.ConnectionServiceI;
import com.payMyBuddy.buddy.service.TransactionServiceI;
import com.payMyBuddy.buddy.service.UserBuddyServiceI;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebMvcTest(controllers = TransferController.class)
@Import(TransferController.class)
@Slf4j
class TransferControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  @Qualifier("userDetailsServiceImpl")
  private UserDetailsService userDetailsService;

  @MockBean
  UserBuddyServiceI userBuddyServiceI;

  @MockBean
  TransactionServiceI transactionServiceI;

  @MockBean
  ConnectionServiceI connectionServiceI;

  @MockBean
  AccountServiceI accountServiceI;

  Iterable<Transaction> listTransaction = new ArrayList<Transaction>();

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        // Utiliser l'object mockMvc avec un contexte sécurisé par SpringSecurity.
        .apply(springSecurity()).build();

  }

  @Test
  void testTransferNotAutorize() throws Exception {
    // ARRANGE
    Set<UserBuddy> contacts = new HashSet<>();
    // ACT AND ASSERT
    this.mockMvc.perform(MockMvcRequestBuilders.get("/transfer")
        .flashAttr("transactions", listTransaction).flashAttr("contacts", contacts))
        .andExpect(status().isUnauthorized());

  }

  @Test
  @WithMockUser("user@gmail.com")
  void testShouldDisplayTransferPage() throws Exception {
    // ARRANGE
    Set<UserBuddy> contacts = new HashSet<>();
    UserBuddy user = new UserBuddy();
    user.setEmail("user@gmail.com");
    user.setId(1L);

    Account account = new Account();
    account.setUserBuddy(user);
    account.setAccountId(1L);

    UserBuddy user2 = new UserBuddy();
    user.setEmail("user2@gmail.com");
    contacts.add(user2);
    user.setContacts(contacts);
    when(userBuddyServiceI.findOne("user@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    when(transactionServiceI.findAllBySenderIdAndType(account, Type.USER_TO_USER))
        .thenReturn(listTransaction);

    // ACT AND ASSERT
    this.mockMvc.perform(MockMvcRequestBuilders.get("/transfer").flashAttr("user", user)
        .flashAttr("transactions", listTransaction).flashAttr("contacts", contacts))
        .andExpect(status().isOk()).andExpect(content().string(containsString("transfer")));

  }

  @Test
  @WithMockUser("user@gmail.com")
  void testPostTransferSuccess() throws Exception {
    // ARRANGE
    Set<UserBuddy> contacts = new HashSet<>();
    UserBuddy user = new UserBuddy();
    user.setEmail("user@gmail.com");
    user.setId(1L);

    Account account = new Account();
    account.setUserBuddy(user);
    account.setAccountId(1L);

    UserBuddy user2 = new UserBuddy();
    user2.setEmail("user2@gmail.com");
    contacts.add(user2);
    user2.setContacts(contacts);

    Account accountB = new Account();
    accountB.setUserBuddy(user2);
    accountB.setAccountId(1L);

    TransactionDto transactionDto = new TransactionDto();
    transactionDto.setAmount(new BigDecimal("100"));

    when(userBuddyServiceI.findOne("user@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    when(transactionServiceI.save(Mockito.any(TransactionDto.class))).thenReturn("success");

    // ACT AND ASSERT
    this.mockMvc
        .perform(post("/transfer/transfer").param("amount", transactionDto.getAmount().toString()))
        .andExpect(redirectedUrl("/transfer?successPayment"));

  }

  @Test
  @WithMockUser("user@gmail.com")
  void testPostTransferNotEnoughMoney() throws Exception {
    // ARRANGE
    Set<UserBuddy> contacts = new HashSet<>();
    UserBuddy user = new UserBuddy();
    user.setEmail("user@gmail.com");
    user.setId(1L);

    Account account = new Account();
    account.setUserBuddy(user);
    account.setAccountId(1L);

    UserBuddy user2 = new UserBuddy();
    user2.setEmail("user2@gmail.com");
    contacts.add(user2);
    user2.setContacts(contacts);

    Account accountB = new Account();
    accountB.setUserBuddy(user2);
    accountB.setAccountId(1L);

    TransactionDto transactionDto = new TransactionDto();
    transactionDto.setAmount(new BigDecimal("100"));

    when(userBuddyServiceI.findOne("user@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    when(transactionServiceI.save(Mockito.any(TransactionDto.class)))
        .thenReturn("errorNotEnoughMoney");

    // ACT AND ASSERT
    this.mockMvc
        .perform(post("/transfer/transfer").param("amount", transactionDto.getAmount().toString()))
        .andExpect(redirectedUrl("/transfer?errorNotEnoughMoney"));

  }

  @Test
  @WithMockUser("user@gmail.com")
  void testPostTransferErrorInactive() throws Exception {
    // ARRANGE
    Set<UserBuddy> contacts = new HashSet<>();
    UserBuddy user = new UserBuddy();
    user.setEmail("user@gmail.com");
    user.setId(1L);

    Account account = new Account();
    account.setUserBuddy(user);
    account.setAccountId(1L);

    UserBuddy user2 = new UserBuddy();
    user2.setEmail("user2@gmail.com");
    contacts.add(user2);
    user2.setContacts(contacts);

    Account accountB = new Account();
    accountB.setUserBuddy(user2);
    accountB.setAccountId(1L);

    TransactionDto transactionDto = new TransactionDto();
    transactionDto.setAmount(new BigDecimal("100"));

    when(userBuddyServiceI.findOne("user@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    when(transactionServiceI.save(Mockito.any(TransactionDto.class))).thenReturn("inactive");

    // ACT AND ASSERT
    this.mockMvc
        .perform(post("/transfer/transfer").param("amount", transactionDto.getAmount().toString()))
        .andExpect(redirectedUrl("/transfer?errorInactive"));

  }

  @Test
  @WithMockUser("user@gmail.com")
  void testPostTransferErrorZero() throws Exception {
    // ARRANGE
    Set<UserBuddy> contacts = new HashSet<>();
    UserBuddy user = new UserBuddy();
    user.setEmail("user@gmail.com");
    user.setId(1L);

    Account account = new Account();
    account.setUserBuddy(user);
    account.setAccountId(1L);

    UserBuddy user2 = new UserBuddy();
    user2.setEmail("user2@gmail.com");
    contacts.add(user2);
    user2.setContacts(contacts);

    Account accountB = new Account();
    accountB.setUserBuddy(user2);
    accountB.setAccountId(1L);

    TransactionDto transactionDto = new TransactionDto();
    transactionDto.setAmount(new BigDecimal("0"));

    when(userBuddyServiceI.findOne("user@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);

    // ACT AND ASSERT
    this.mockMvc
        .perform(post("/transfer/transfer").param("amount", transactionDto.getAmount().toString()))
        .andExpect(redirectedUrl("/transfer?errorZero"));

  }

}
