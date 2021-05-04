package com.payMyBuddy.buddy;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.payMyBuddy.buddy.configuration.SecurityConfig;
import com.payMyBuddy.buddy.controller.HomeController;
import com.payMyBuddy.buddy.dto.TransactionDto;
import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.Role;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.service.AccountServiceI;
import com.payMyBuddy.buddy.service.BankAccountServiceI;
import com.payMyBuddy.buddy.service.TransactionServiceI;
import com.payMyBuddy.buddy.service.UserBuddyServiceI;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebMvcTest(controllers = HomeController.class)
@Import(HomeController.class)
@Slf4j
class HomeControllerTest {

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
  BankAccountServiceI bankAccountServiceI;

  @MockBean
  AccountServiceI accountServiceI;
  Account account = new Account();
  UserBuddy user = new UserBuddy();
  
  @Before
  public void setup(){
   mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext)
                           //Utiliser l'object mockMvc avec un contexte sécurisé par SpringSecurity.
                           .apply(springSecurity()).build();

  }

  
  @Test
  @WithMockUser("user@gmail.com")
  void testShouldReturnDefaultMessage() throws Exception {
    //ARRANGE
    Collection<Role> roles = new HashSet<Role>();
    roles.add(new Role("ROLE_USER"));
    user.setEmail("user@gmail.com");
    user.setPassword("user");
    user.setRoles(roles);
    account.setBalance(new BigDecimal("0"));
    account.setUserBuddy(user);
    when(userBuddyServiceI.findOne("user@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    
    //ACT AND ASSERT
    this.mockMvc.perform(MockMvcRequestBuilders.get("/home").flashAttr("account", account)).andExpect(status().isOk())
        .andExpect(content().string(containsString("home")));
    
  }
  
  @Test
  void testHomeNotAutorize() throws Exception {
    //ARRANGE
   
    
    //ACT AND ASSERT
    this.mockMvc.perform(MockMvcRequestBuilders.get("/home").flashAttr("account", account)).andExpect(status().isUnauthorized());
    
  }
  
  @Test
  @WithMockUser("admin@gmail.com")
  void testHomeAdmin() throws Exception {
    //ARRANGE
    Collection<Role> roles = new HashSet<Role>();
    roles.add(new Role("ROLE_ADMIN"));
    user.setEmail("admin@gmail.com");
    user.setPassword("admin");
    user.setRoles(roles);
    account.setBalance(new BigDecimal("0"));
    account.setUserBuddy(user);
    when(userBuddyServiceI.findOne("admin@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    
    //ACT AND ASSERT
    this.mockMvc.perform(MockMvcRequestBuilders.get("/home").flashAttr("account", account)).andExpect(status().isOk())
        .andExpect(content().string(containsString("admin")));
    
  }
  
  @Test
  void testPostNewBankDetails() throws Exception {
    // ACT
    mockMvc
        .perform(post("/home/addBankAccount"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(redirectedUrl("/home?successAddBankAccount"));
    
  }
  
  @Test
  @WithMockUser("user@gmail.com")
  void testTransfer() throws Exception {
    // ARRANGE
    TransactionDto transac = new TransactionDto();
    UserBuddy user =new UserBuddy();
    user.setEmail("user@gmail.com");
    Account account = new Account();
    account.setBalance(new BigDecimal("200"));
    account.setUserBuddy(user);
    when(userBuddyServiceI.findOne("user@gmail.com")).thenReturn(user);
    when(accountServiceI.findByUserAccountId(user)).thenReturn(account);
    when(transactionServiceI.save(transac)).thenReturn("success");
    
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
