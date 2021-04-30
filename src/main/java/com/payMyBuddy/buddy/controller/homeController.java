package com.payMyBuddy.buddy.controller;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.payMyBuddy.buddy.dto.BankAccountDto;
import com.payMyBuddy.buddy.dto.TransactionDto;
import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.BankAccount;
import com.payMyBuddy.buddy.model.Role;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.service.AccountServiceI;
import com.payMyBuddy.buddy.service.BankAccountServiceI;
import com.payMyBuddy.buddy.service.TransactionServiceI;
import com.payMyBuddy.buddy.service.UserBuddyServiceI;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/home")
@Validated
public class homeController {

  @Autowired
  AccountServiceI AccountServiceI;

  @Autowired
  UserBuddyServiceI userBuddyServiceI;

  @Autowired
  TransactionServiceI transactionServiceI;

  @Autowired
  BankAccountServiceI bankAccountServiceI;

  @ModelAttribute("bankAccountadd")
  public BankAccountDto bankAccountDto() {
    return new BankAccountDto();
  }

  @ModelAttribute("withdraw")
  public TransactionDto transactionDto() {
    return new TransactionDto();
  }

  @GetMapping
  public String home(Model model) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();

    UserBuddy user = userBuddyServiceI.findOne(username);

    Account account = AccountServiceI.findByUserAccountId(user);
    BankAccount bankAccount = user.getBankAccount();
    model.addAttribute("account", account);
    model.addAttribute("bankAccount", bankAccount);

    Collection<Role> roles = user.getRoles();
    if (roles.toString().contains("ROLE_ADMIN")) {
      return "admin";
    }
    return "home";
  }

  @PostMapping
  public String transfer(@ModelAttribute("transaction") TransactionDto transactionDto) {

    if (transactionDto.getAmount().compareTo(BigDecimal.ZERO) > 0) {

      String reponse = transactionServiceI.save(transactionDto);
      if (reponse == "success") {
        return "redirect:/home?successPayment";
      } else if (reponse == "errorNotEnoughMoney") {
        return "redirect:/home?errorNotEnoughMoney";
      } else {
        return "redirect:/home?errorZero";
      }
    } else {
      return "redirect:/home?error";
    }
  }

  @PostMapping("/addBankAccount")
  public String addBankAccount(@ModelAttribute("bankAccountadd") BankAccountDto bankAccountDto) {

    
    bankAccountServiceI.save(bankAccountDto);

    return "redirect:/home?successAddBankAccount";

  }
}
