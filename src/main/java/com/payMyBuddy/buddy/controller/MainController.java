package com.payMyBuddy.buddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
  /**
   * Login page
   * @return login page
   */
  @GetMapping("/login")
  public String login() {
    return "login";
    }

}
