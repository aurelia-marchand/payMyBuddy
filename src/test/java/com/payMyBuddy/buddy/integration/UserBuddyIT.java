package com.payMyBuddy.buddy.integration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.payMyBuddy.buddy.dto.UserProfileDto;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;
import com.payMyBuddy.buddy.service.UserBuddyServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@Sql("/insert-data.sql")
class UserBuddyIT {
  
  @Autowired
  UserBuddyServiceImpl userBuddyServiceImpl;
  @Autowired
  UserBuddyRepository userBuddyRepository;
  

  @Test
  @Transactional
  @WithMockUser(username = "aur@gmail.com")
  void testfindUserByEmail() {
    
    UserBuddy user = userBuddyRepository.findByemail("aur@gmail.com");
    
    log.debug("user : " + user);
    assertThat(user.getFirstName()).isEqualToIgnoringCase("aurelia");
    
  }
  
  @Test
  @Transactional
  @WithMockUser(username = "aur@gmail.com")
  void testUpdateProfile() {
    //ARRANGE
    UserProfileDto userDto = new UserProfileDto();
    userDto.setEmail("aur@gmail.com");
    userDto.setLastName("theret");
   
    //ACT
    UserBuddy userUpdate = userBuddyServiceImpl.save(userDto);
    
    //ASSERT
    assertThat(userUpdate.getLastName()).isEqualToIgnoringCase("theret");
    
    UserBuddy userTest = userBuddyRepository.findByemail("aur@gmail.com");
    log.debug("lastname : " + userTest.getLastName());
  }

}
