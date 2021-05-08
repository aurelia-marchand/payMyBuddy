package com.payMyBuddy.buddy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payMyBuddy.buddy.constant.BuddyConstant;
import com.payMyBuddy.buddy.dto.TransactionDto;
import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.Transaction;
import com.payMyBuddy.buddy.model.Type;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.AccountRepository;
import com.payMyBuddy.buddy.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionServiceI {

  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  UserBuddyServiceI userBuddyServiceI;

  @Autowired
  AccountServiceI accountServiceI;

  @Autowired
  BankPayment bankPayment;

  @Autowired
  AccountRepository accountRepository;

  @Override
  public Iterable<Transaction> findAllBySenderId(Account account) {
    return transactionRepository.findAllBySenderId(account);
  }

  @Override
  @Transactional
  public String save(TransactionDto transactionDto) {
    // preparing the entity to persist
    Transaction transactionPayment = new Transaction();

    Account account = transactionDto.getSenderId();
    // update transaction with Dto received
    transactionPayment.setAmount(transactionDto.getAmount());
    transactionPayment.setDateTransaction(LocalDate.now());
    transactionPayment.setDescription(transactionDto.getDescription());
    transactionPayment.setType(transactionDto.getType());
    transactionPayment.setSenderId(account);

    // preparing entity beneficiary
    Account accountB = new Account();

    // different type transaction
    if (transactionDto.getType() == Type.USER_TO_USER) {

      // recovery beneficiary mail and the user to find this account
      String email = transactionDto.getMailBeneficiary();
      UserBuddy userB = userBuddyServiceI.findOne(email);
      // Verify if account beneficiary is active before transaction
      if(!userB.isActive()) {
        return "inactive";
      }
      accountB = accountServiceI.findByUserAccountId(userB);

      // update beneficiary account
      transactionPayment.setBeneficiaryId(accountB);

      transactionPayment
          .setFee(transactionDto.getAmount().multiply(new BigDecimal(BuddyConstant.FEE)).setScale(2, RoundingMode.HALF_UP));
      log.debug("frais : " + transactionPayment.getFee());

      // verify amount and balance account before doing the transaction
      if (transactionDto.getType().equals(Type.USER_TO_USER) && account.getBalance()
          .compareTo(transactionDto.getAmount().add(transactionPayment.getFee())) < 0) {
        return "errorNotEnoughMoney";
      }

    } else {
      // verify amount and balance account before doing the transaction
      if (transactionDto.getDescription().equalsIgnoreCase("withdraw")
          && account.getBalance().compareTo(transactionDto.getAmount()) < 0) {
        return "errorNotEnoughMoney";
      }
      transactionPayment.setType(Type.BANK_TRANSFER);
      transactionPayment.setBeneficiaryId(account);
      transactionPayment.setFee(new BigDecimal("0"));
    }

    // TODO send requestAuthorization just for type with bank account and send money
    // to admin bank
    // bankPayment is an interface before real implementation link with bank of
    // users and admin webApp, just return true for now
    if (bankPayment.requestAuthorization(transactionPayment)) {

      // if bank return true save transaction else return error
      transactionRepository.save(transactionPayment);

      // update account balance for withdraw, payment and between user
      Account accountToUpdate = accountRepository.getOne(account.getAccountId());
      // for withdraw
      if (transactionPayment.getDescription().equalsIgnoreCase("withdraw")) {
        accountToUpdate.setBalance(account.getBalance().subtract(transactionPayment.getAmount()));
        // for payment
      } else if (transactionPayment.getDescription().equalsIgnoreCase("payment")) {
        accountToUpdate.setBalance(account.getBalance().add(transactionPayment.getAmount()));
        // for transfer between account user
      } else {
        accountToUpdate.setBalance(account.getBalance().subtract(transactionPayment.getAmount()));
        accountToUpdate.setBalance(account.getBalance().subtract(transactionPayment.getFee()));
        Account accountBeneficiary = accountRepository.getOne(accountB.getAccountId());
        accountBeneficiary.setBalance(accountB.getBalance().add(transactionPayment.getAmount()));
        accountRepository.save(accountBeneficiary);
      }
      accountRepository.save(accountToUpdate);
      return "success";
    } else {
      return "error";
    }
  }

  @Override
  public Iterable<Transaction> findAllBySenderIdAndType(Account account, Type userToUser) {
    return transactionRepository.findAllBySenderIdAndType(account, userToUser);

  }

}
