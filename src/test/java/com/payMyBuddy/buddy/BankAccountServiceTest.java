package com.payMyBuddy.buddy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.payMyBuddy.buddy.dto.BankAccountDto;
import com.payMyBuddy.buddy.model.BankAccount;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.BankAccountRepository;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;
import com.payMyBuddy.buddy.service.BankAccountServiceImpl;
import com.payMyBuddy.buddy.service.UserBuddyServiceI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(BankAccountServiceImpl.class)
@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {
  
  @MockBean
  @Qualifier("userDetailsServiceImpl")
  private UserDetailsService userDetailsService;
  
  @MockBean
  BankAccountRepository bankAccountRepository;
  
  @MockBean
  UserBuddyServiceI userBuddyServiceI;
  
  @MockBean
  UserBuddyRepository userBuddyRepository;
  
  @Autowired
  BankAccountServiceImpl bankAccountServiceImpl;

  
  
  
  @Test
  void test() {
    
    BankAccountDto bankAccountDto = new BankAccountDto();
    BankAccount bankAccount = new BankAccount();
    bankAccount.setAccountNumber("100s85s6669800000");
    bankAccount.setBankAccountId(1L);
    bankAccount.setBankCounterCode(10005);
    bankAccount.setBic("psstrf520pptd");
    bankAccount.setCodeBank(20005);
    bankAccount.setDomiciliation("FR lyon");
    bankAccount.setHolder("user user");
    bankAccount.setIban("100s85s6669800000");
    bankAccount.setRibKey(32);
    
    
    bankAccountDto.setAccountNumber("100s85s6669800000");
    bankAccountDto.setBankAccountId(1L);
    bankAccountDto.setBankCounterCode(10005);
    bankAccountDto.setBic("psstrf520pptd");
    bankAccountDto.setCodeBank(20005);
    bankAccountDto.setDomiciliation("FR lyon");
    bankAccountDto.setEmail("user@gmail.com");
    bankAccountDto.setHolder("user user");
    bankAccountDto.setIban("100s85s6669800000");
    bankAccountDto.setRibKey(32);
    
    UserBuddy user = new UserBuddy();
    user.setEmail("user@gmail.com");
    user.setId(1L);
    
    UserBuddy userSave = new UserBuddy();
    userSave.setEmail("user@gmail.com");
    userSave.setId(1L);
    
    when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
    when(userBuddyServiceI.findOne(bankAccountDto.getEmail())).thenReturn(user);
    when(userBuddyRepository.getOne(user.getId())).thenReturn(userSave);
    
    when(userBuddyRepository.save(userSave)).thenReturn(userSave);

    //ACT
    String result = bankAccountServiceImpl.save(bankAccountDto);
    assertThat(result).isEqualToIgnoringCase("success");
    
    BankAccount bankA = userSave.getBankAccount();
    assertThat(bankA.getHolder()).isEqualToIgnoringCase("user user");
    
  }

}
