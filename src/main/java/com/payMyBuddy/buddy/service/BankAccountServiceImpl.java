package com.payMyBuddy.buddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payMyBuddy.buddy.dto.BankAccountDto;
import com.payMyBuddy.buddy.model.BankAccount;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.BankAccountRepository;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
    
    log.debug("bDto"+ bankAccountDto);
    
    bankAccount.setCodeBank(bankAccountDto.getCodeBank());
    bankAccount.setBankCounterCode(bankAccountDto.getBankCounterCode());
    bankAccount.setAccountNumber(bankAccountDto.getAccountNumber());
    bankAccount.setRibKey(bankAccountDto.getRibKey());
    bankAccount.setDomiciliation(bankAccountDto.getDomiciliation());
    bankAccount.setIban(bankAccountDto.getIban());
    bankAccount.setBic(bankAccountDto.getBic());
    bankAccount.setHolder(bankAccountDto.getHolder());
    
    log.debug("bankAccount"+ bankAccount);

    bankAccountRepository.save(bankAccount);
   
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
//    String username = authentication.getName();
    
    UserBuddy user = userBuddyServiceI.findOne(bankAccountDto.getEmail());
    log.debug("user"+ user);

    Long id = user.getId();
    log.debug("id"+ id);

    UserBuddy userToUpdate = userBuddyRepository.getOne(id);
    log.debug("userToUpdate"+ userToUpdate);

    userToUpdate.setBankAccount(bankAccount);
    userBuddyRepository.save(userToUpdate);
    log.debug("userToUpdate"+ userToUpdate);
    return "success";
    
  }

}
