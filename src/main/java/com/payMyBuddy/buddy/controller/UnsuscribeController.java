package com.payMyBuddy.buddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.payMyBuddy.buddy.dto.UserDto;
import com.payMyBuddy.buddy.service.UserBuddyServiceI;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/unsuscribe")
public class UnsuscribeController {
  
  @Autowired
  UserBuddyServiceI userBuddyServiceI;

  /**
   * Unsuscribe page
   * @param model
   * @return
   */
  @GetMapping
  public String unsuscribe(Model model) {
    
    return "unsuscribe";
  }
  
  /**
   * Form to unsuscribe
   * @param userDto
   * @return
   */
  @PostMapping
  public String transfer(@ModelAttribute("user") UserDto userDto) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    
    userDto.setEmailConfirm(username);
    
    if (!userDto.getEmail().equalsIgnoreCase(userDto.getEmailConfirm())) {
      return "redirect:/unsuscribe?error";
  }
      userBuddyServiceI.unsuscribe(userDto);
      return "redirect:/login?unsuscribe";
    }
}
