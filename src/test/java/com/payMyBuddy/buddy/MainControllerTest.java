package com.payMyBuddy.buddy;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.payMyBuddy.buddy.configuration.SecurityConfig;
import com.payMyBuddy.buddy.controller.MainController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebMvcTest(controllers = MainController.class)
@Import(MainController.class)
class MainControllerTest {
  
  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  @Qualifier("userDetailsServiceImpl")
  private UserDetailsService userDetailsService;

  @Test
  void testShouldReturnDefaultMessage() throws Exception {
    this.mockMvc.perform(get("/login")).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("login")));

  }

}
