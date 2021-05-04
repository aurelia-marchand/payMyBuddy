package com.payMyBuddy.buddy.service;

import java.util.List;

import com.payMyBuddy.buddy.dto.UserProfileDto;
import com.payMyBuddy.buddy.dto.UserRegistrationDto;
import com.payMyBuddy.buddy.model.UserBuddy;

public interface UserBuddyServiceI {
  
  UserBuddy save(UserRegistrationDto userRegistrationDto);

  List<UserBuddy> findAll();
  
  UserBuddy findOne(String email);

  boolean existsUserBuddyByEmail(String email);

  UserBuddy save(UserProfileDto userDto);
  
}
