package com.payMyBuddy.buddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class homeController {
  
  
  @GetMapping("/home")
  public String home() {
    log.debug("controller home");
    return "home";
  
  }
}
