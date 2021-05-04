package com.payMyBuddy.buddy.dto;

import lombok.Data;

@Data
public class BankAccountDto {
  
  private String email;

  private long bankAccountId;

  private Integer codeBank;

  private Integer bankCounterCode;

  private String accountNumber;

  private Integer ribKey;

  private String domiciliation;

  private String iban;

  private String bic;

  private String holder;

}
