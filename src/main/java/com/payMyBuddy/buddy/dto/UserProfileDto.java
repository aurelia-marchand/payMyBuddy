package com.payMyBuddy.buddy.dto;

import javax.validation.constraints.Max;

import lombok.Data;

@Data
public class UserProfileDto {

  
  private String email;
  
  private String lastName;
  
 
  private String firstName;


  private String birthdate;

  private String address;

  @Max(value=5, message="size problem")
  private int zip;

  private String city;

  private String phone;
}
