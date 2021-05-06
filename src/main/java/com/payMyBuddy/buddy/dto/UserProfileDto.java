package com.payMyBuddy.buddy.dto;

import lombok.Data;

@Data
public class UserProfileDto {

  private String email;
  
  private String lastName;
  
  private String firstName;

  private String birthdate;

  private String address;

  private int zip;

  private String city;

  private String phone;
}
