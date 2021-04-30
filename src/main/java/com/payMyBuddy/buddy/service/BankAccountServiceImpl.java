package com.payMyBuddy.buddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.payMyBuddy.buddy.dto.BankAccountDto;
import com.payMyBuddy.buddy.model.BankAccount;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.BankAccountRepository;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;

@Service
public class BankAccountServiceImpl implements BankAccountServiceI{

  @Autowired
  BankAccountRepository bankAccountRepository;
  
  @Autowired
  UserBuddyServiceI userBuddyServiceI;
  
  @Autowired
  UserBuddyRepository userBuddyRepository;
  
 
  
  @Override
  public String save(BankAccountDto bankAccountDto) {
    //TODO look type return
    BankAccount bankAccount = new BankAccount();
    
    bankAccount.setCodeBank(bankAccountDto.getCodeBank());
    bankAccount.setBankCounterCode(bankAccountDto.getBankCounterCode());
    bankAccount.setAccountNumber(bankAccountDto.getAccountNumber());
    bankAccount.setRibKey(bankAccountDto.getRibKey());
    bankAccount.setDomiciliation(bankAccountDto.getDomiciliation());
    bankAccount.setIban(bankAccountDto.getIban());
    bankAccount.setBic(bankAccountDto.getBic());
    bankAccount.setHolder(bankAccountDto.getHolder());
    
    bankAccountRepository.save(bankAccount);
   
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
    String username = authentication.getName();
    
    UserBuddy user = userBuddyServiceI.findOne(username);
    Long id = user.getId();
    UserBuddy userToUpdate = userBuddyRepository.getOne(id);
    userToUpdate.setBankAccount(bankAccount);
    userBuddyRepository.save(userToUpdate);
 
    return "success";
    
  }

}
