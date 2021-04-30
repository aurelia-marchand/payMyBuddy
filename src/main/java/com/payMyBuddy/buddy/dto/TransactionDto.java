package com.payMyBuddy.buddy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.Type;
import com.payMyBuddy.buddy.model.UserBuddy;

import lombok.Data;

@Data
public class TransactionDto {
  
  private long transactionId;

  private BigDecimal amount;

  private LocalDate dateTransaction;

  private BigDecimal fee;

  private String description;
 
  private Type type;
  
  private Account senderId;
  
  private Account beneficiaryId;
  private Set<UserBuddy> contacts;
  private String mailBeneficiary;

}
