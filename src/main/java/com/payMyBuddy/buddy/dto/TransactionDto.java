package com.payMyBuddy.buddy.dto;

import java.math.BigDecimal;

import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.Type;

import lombok.Data;

@Data
public class TransactionDto {
  
  private BigDecimal amount;
  private String description;
  private Type type;
  private Account senderId;  
  private Account beneficiaryId;
  private String mailBeneficiary;

}
