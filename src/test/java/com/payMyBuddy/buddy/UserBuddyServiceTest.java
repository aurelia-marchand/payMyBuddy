package com.payMyBuddy.buddy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.payMyBuddy.buddy.dto.UserDto;
import com.payMyBuddy.buddy.dto.UserProfileDto;
import com.payMyBuddy.buddy.dto.UserRegistrationDto;
import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;
import com.payMyBuddy.buddy.service.AccountServiceI;
import com.payMyBuddy.buddy.service.UserBuddyServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(UserBuddyServiceImpl.class)
@ExtendWith(MockitoExtension.class)
class UserBuddyServiceTest {
  
  @MockBean
  @Qualifier("userDetailsServiceImpl")
  private UserDetailsService userDetailsService;

  @MockBean
  UserBuddyRepository userBuddyRepository;

  @MockBean
  AccountServiceI accountServiceI;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  UserBuddyServiceImpl userBuddyServiceImpl;

  UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
  UserBuddy user = new UserBuddy();
  UserBuddy userSave = new UserBuddy();
  Account account = new Account();

  @BeforeEach
  private void setUpPerTest() {

  }

  @Test
  void testUserSaveAndCreateAnAccount() {
    // ARRANGE
    userRegistrationDto.setEmail("user1@gmail.com");
    userRegistrationDto.setPassword("user");

    userSave.setEmail("user1@gmail.com");
    userSave.setPassword("$2a$10$zTw9tVKQ8YVat8G2uc2W4O3xko1AB4UAZDYrofKPyn7.uxqt9OCQ2");

    account.setUserBuddy(userSave);

    when(userBuddyRepository.save(Mockito.any(UserBuddy.class))).thenReturn(userSave);
    when(accountServiceI.save(userSave)).thenReturn("success");

    // ACT
    userBuddyServiceImpl.save(userRegistrationDto);

    // ASSERT
    verify(userBuddyRepository, times(1)).save(Mockito.any(UserBuddy.class));
    verify(accountServiceI, times(1)).save(Mockito.any(UserBuddy.class));

  }

  @Test
  void testFindAll() {
    // ACT
    userBuddyServiceImpl.findAll();
    // ASSERT
    verify(userBuddyRepository, times(1)).findAll();

  }

  @Test
  void testFindOne() {
    //ACT
    userBuddyServiceImpl.findOne("mail");
    //ASSERT
    verify(userBuddyRepository, times(1)).findByemail("mail");
  }

  @Test
  void userSet() {
    // Arrange
    userRegistrationDto.setEmail("user1@gmail.com");
    userRegistrationDto.setPassword("user");
    // Act
    UserBuddy user = userBuddyServiceImpl.userSet(userRegistrationDto);
    // ASSERT
    assertThat(user.getPassword()).isNotEqualToIgnoringCase("user");
    assertThat(user.getRoles()).toString().contains("ROLE_USER");

  }
  
  @Test
  void testUserProfileSave() {
    //ARRANGE
    UserProfileDto userDto = new UserProfileDto();
    userDto.setEmail("aur@gmail.com");
    userDto.setFirstName("aurelia");
    userDto.setLastName("marchand");
    userDto.setBirthdate("13/04/1984");
    userDto.setAddress("rue de la plaine");
    userDto.setZip(95420);
    userDto.setCity("magny");
    userDto.setPhone("0626556485");
    
    
    user.setId(1L);
    user.setEmail("aur@gmail.com");
    user.setPassword("user");
    
    userSave.setId(1L);
    userSave.setEmail("aur@gmail.com");
    userSave.setPassword("user");
    
    when(userBuddyRepository.findByemail(user.getEmail())).thenReturn(user);
    when(userBuddyRepository.getOne(user.getId())).thenReturn(userSave);
    when(userBuddyRepository.save(userSave)).thenReturn(new UserBuddy());
    
    //ACT
    userBuddyServiceImpl.save(userDto);
    
    //ASSERT
    assertThat(userSave.getFirstName()).isEqualToIgnoringCase("aurelia");
    verify(userBuddyRepository).save(userSave);

  }
  
  @Test
  void testExistUserByEmail() {
    //ACT
    userBuddyServiceImpl.existsUserBuddyByEmail("mail");
    //ASSERT
    verify(userBuddyRepository, times(1)).existsUserBuddyByEmail("mail");
  }
  
  @Test
  void testUnsuscribe() {
    //ARRANGE
    UserDto userDto = new UserDto();
    userDto.setEmail("email");
    userDto.setEmailConfirm("email");

    UserBuddy user = new UserBuddy();
    user.setActive(true);
    user.setEmail("email");
    user.setPassword("user");
    
    UserBuddy userToSave = new UserBuddy();
    userToSave.setActive(true);
    userToSave.setEmail("email");
    userToSave.setPassword("user");
    
    when(userBuddyRepository.findByemail(userDto.getEmail())).thenReturn(user);
    when(userBuddyRepository.getOne(user.getId())).thenReturn(userToSave);
    when(userBuddyRepository.save(userToSave)).thenReturn(userToSave);
    //ACT
    UserBuddy userSave = userBuddyServiceImpl.unsuscribe(userDto);
    //ASSERT
    assertThat(userSave.isActive()).isEqualTo(false);
  }

}
