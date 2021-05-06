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
    BankAccount bankAccount = new BankAccount();
    
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
    
    UserBuddy user = userBuddyServiceI.findOne(bankAccountDto.getEmail());
    Long id = user.getId();

    // add bank account to entity userBuddy
    UserBuddy userToUpdate = userBuddyRepository.getOne(id);
    log.debug("userToUpdate"+ userToUpdate);
    userToUpdate.setBankAccount(bankAccount);
    userBuddyRepository.save(userToUpdate);
    return "success";
    
  }

}
