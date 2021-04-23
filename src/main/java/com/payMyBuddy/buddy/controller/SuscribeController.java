package com.payMyBuddy.buddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.payMyBuddy.buddy.dto.UserRegistrationDto;
import com.payMyBuddy.buddy.service.RoleServiceI;
import com.payMyBuddy.buddy.service.UserBuddyServiceI;

@Controller
@RequestMapping("/suscribe")
public class SuscribeController {

  @Autowired
  UserBuddyServiceI userBuddyServiceI;
  
  @Autowired
  RoleServiceI roleServiceI;
  
  @ModelAttribute("user")
  public UserRegistrationDto userRegistrationDto() {
    return new UserRegistrationDto();
  }
  
  @GetMapping
  public String showRegistrationForm() {
    return "suscribe";
  }
  
  @PostMapping
  public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto userRegistrationDto) {
    userBuddyServiceI.save(userRegistrationDto);
    
    return "redirect:/suscribe?success";
    }
  
}
