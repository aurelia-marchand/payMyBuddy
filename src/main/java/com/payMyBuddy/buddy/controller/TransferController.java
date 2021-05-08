package com.payMyBuddy.buddy.controller;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.payMyBuddy.buddy.dto.TransactionDto;
import com.payMyBuddy.buddy.dto.UserConnectionDto;
import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.Transaction;
import com.payMyBuddy.buddy.model.Type;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.service.AccountServiceI;
import com.payMyBuddy.buddy.service.ConnectionServiceI;
import com.payMyBuddy.buddy.service.TransactionServiceI;
import com.payMyBuddy.buddy.service.UserBuddyServiceI;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/transfer")
public class TransferController {

  @Autowired
  UserBuddyServiceI userBuddyServiceI;

  @Autowired
  TransactionServiceI transactionServiceI;

  @Autowired
  ConnectionServiceI connectionServiceI;

  @Autowired
  AccountServiceI accountServiceI;

  @ModelAttribute("transaction")
  public TransactionDto transactionDto() {
    return new TransactionDto();
  }

  /**
   * Transfer page, display form transfer, connection and all transfers
   * @param model
   * @return
   */
  @GetMapping
  public String transfer(Model model) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    UserBuddy user = userBuddyServiceI.findOne(username);
    Account account = accountServiceI.findByUserAccountId(user);

    //To display transfers and connections
    Iterable<Transaction> listTransaction = null;
    listTransaction = transactionServiceI.findAllBySenderIdAndType(account, Type.USER_TO_USER);
    Set<UserBuddy> contacts = user.getContacts();
    model.addAttribute("transactions", listTransaction);
    model.addAttribute("contacts", contacts);

    return "transfer";
  }

  /**
   * Form to add connection
   * @param userConnectionDto
   * @return
   */
  @PostMapping("/connection")
  public String addConnection(@ModelAttribute("user") UserConnectionDto userConnectionDto) {
    if (userBuddyServiceI.existsUserBuddyByEmail(userConnectionDto.getEmail())) {
      connectionServiceI.add(userConnectionDto);
      return "redirect:/transfer?successAddConnection";
    } else {
      return "redirect:/transfer?errorAddConnection";
    }
  }

  /**
   * Form transfer
   * @param transactionDto
   * @return
   */
  @PostMapping("/transfer")
  public String transfer(@ModelAttribute("transaction") TransactionDto transactionDto) {

    // recovery identity user connected, the entity and this account
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    UserBuddy user = userBuddyServiceI.findOne(username);
    Account account = accountServiceI.findByUserAccountId(user);
    if (transactionDto.getAmount().compareTo(BigDecimal.ZERO) > 0) {
      transactionDto.setSenderId(account);
      transactionDto.setType(Type.USER_TO_USER);
      log.debug("transactionController : " + transactionDto.getSenderId());
      String reponse = transactionServiceI.save(transactionDto);
      log.debug("reponse : "+ reponse);
      log.debug("transactionDto : "+ transactionDto);

      if (reponse == "success") {
        return "redirect:/transfer?successPayment";
      } else if (reponse == "errorNotEnoughMoney") {
        return "redirect:/transfer?errorNotEnoughMoney";
      } else if (reponse == "inactive") {
        return "redirect:/transfer?errorInactive";
      }
    }
    return "redirect:/transfer?errorZero";

  }
}
