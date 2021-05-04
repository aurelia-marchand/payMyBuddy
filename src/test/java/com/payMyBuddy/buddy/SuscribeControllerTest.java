package com.payMyBuddy.buddy;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.payMyBuddy.buddy.configuration.SecurityConfig;
import com.payMyBuddy.buddy.controller.SuscribeController;
import com.payMyBuddy.buddy.dto.UserRegistrationDto;
import com.payMyBuddy.buddy.service.RoleServiceI;
import com.payMyBuddy.buddy.service.UserBuddyServiceI;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SecurityConfig.class)
@WebMvcTest(controllers = SuscribeController.class)
@Import(SuscribeController.class)
class SuscribeControllerTest {

  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  @Qualifier("userDetailsServiceImpl")
  private UserDetailsService userDetailsService;

  @MockBean
  UserBuddyServiceI userBuddyServiceI;

  @MockBean
  RoleServiceI roleServiceI;

  @Test
  void testShouldReturnDefaultMessage() throws Exception {
    this.mockMvc.perform(get("/suscribe")).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString("suscribe")));

  }

  @Test
  void testPostNewUser() throws Exception {
    // ARRANGE
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

    RequestBuilder request = post("/suscribe")
        .param("email", userRegistrationDto.getEmail())
        .param("password", userRegistrationDto.getPassword());

    // ACT
    mockMvc
        .perform(request)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(redirectedUrl("/suscribe?successRegistration"));
    // ASSERT
  }
  
  @Test
  void testPostNewUserIfExist() throws Exception {
    // ARRANGE
    UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
    userRegistrationDto.setEmail("user1@gmail.com");
    userRegistrationDto.setPassword("user");
    
    when(userBuddyServiceI.existsUserBuddyByEmail(userRegistrationDto.getEmail())).thenReturn(true);

    RequestBuilder request = post("/suscribe")
        .param("email", userRegistrationDto.getEmail())
        .param("password", userRegistrationDto.getPassword());

    // ACT
    mockMvc
        .perform(request)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(redirectedUrl("/suscribe?error"));
    // ASSERT
  }

}
