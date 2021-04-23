
package com.payMyBuddy.buddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table (name = "bank_account")
public class BankAccount {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="bank_account_id")
  private long bankAccountId;
  
  @Column(name="code_bank")
	private int codeBank;
	/**
	 * 
	 */
  @Column(name="bank_counter_code")
	private int bankCounterCode;
	/**
	 * 
	 */
  @Column(name="account_number")
	private String accountNumber;
	/**
	 * 
	 */
  @Column(name="rib_key")
	private int ribKey;
	/**
	 * 
	 */
	private String domiciliation;
	/**
	 * 
	 */
	private String iban;
	/**
	 * 
	 */
	private String bic;
	
	private String holder;
	
};
