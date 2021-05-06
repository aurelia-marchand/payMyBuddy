package com.payMyBuddy.buddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.payMyBuddy.buddy.dto.UserRegistrationDto;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.service.RoleServiceI;
import com.payMyBuddy.buddy.service.UserBuddyServiceI;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/suscribe")
@Slf4j
public class SuscribeController {

  @Autowired
  UserBuddyServiceI userBuddyServiceI;

  @Autowired
  RoleServiceI roleServiceI;

  @ModelAttribute("user")
  public UserRegistrationDto userRegistrationDto() {
    return new UserRegistrationDto();
  }

  /**
   * Suscribe Page
   * @return
   */
  @GetMapping
  public String showRegistrationForm() {
    return "suscribe";
  }

  /**
   * Form suscribe
   * @param userRegistrationDto
   * @return
   */
  @PostMapping
  public String registerUserAccount(
      @ModelAttribute("user") UserRegistrationDto userRegistrationDto) {

    //Verify if user already exist or account inactive
    UserBuddy user = userBuddyServiceI.findOne(userRegistrationDto.getEmail());
    Boolean active = user.isActive();
    
    if(!active) {
      return "redirect:/suscribe?inactive";
    }
    if (userBuddyServiceI.existsUserBuddyByEmail(userRegistrationDto.getEmail())) {
      return "redirect:/suscribe?error";
    }
     else {
      userBuddyServiceI.save(userRegistrationDto);
      return "redirect:/suscribe?successRegistration";
    }

  }

}
