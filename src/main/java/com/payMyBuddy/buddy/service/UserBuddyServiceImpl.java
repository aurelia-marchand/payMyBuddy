package com.payMyBuddy.buddy.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payMyBuddy.buddy.dto.UserProfileDto;
import com.payMyBuddy.buddy.dto.UserRegistrationDto;
import com.payMyBuddy.buddy.model.Role;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserBuddyServiceImpl implements UserBuddyServiceI {

  @Autowired
  private UserBuddyRepository userBuddyRepository;

  @Autowired
  private AccountServiceI accountServiceI;

  @Autowired
  private PasswordEncoder passwordEncoder;
  
  

  @Override
  @Transactional
  public UserBuddy save(UserRegistrationDto userRegistrationDto) {
    
    UserBuddy userSet = userSet(userRegistrationDto);
    UserBuddy userBuddy = userBuddyRepository.save(userSet);
    accountServiceI.save(userBuddy);
    
    return userBuddy;
  }
  
  /**
   * Set Email and encode password before save in database
   * @param userRegistrationDto
   * @return user to save
   */
  public UserBuddy userSet(UserRegistrationDto userRegistrationDto) {
    UserBuddy user = new UserBuddy();
    
    user.setEmail(userRegistrationDto.getEmail());
    user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
    if(userRegistrationDto.getEmail().equalsIgnoreCase("admin4@paymybuddy.com")) {
      user.setRoles(Arrays.asList(new Role("ROLE_ADMIN")));
    }else {
      user.setRoles(Arrays.asList(new Role("ROLE_USER")));
    }
    
    return user;
  }

  @Override
  public List<UserBuddy> findAll() {
    return userBuddyRepository.findAll();
  }

  @Override
  public UserBuddy findOne(String email) {
    return userBuddyRepository.findByemail(email);
  }

  

  @Override
  public boolean existsUserBuddyByEmail(String email) {

    return userBuddyRepository.existsUserBuddyByEmail(email);
  }

  @Override
  public UserBuddy save(UserProfileDto userDto) {
    UserBuddy user = new UserBuddy();
  
    user = userBuddyRepository.findByemail(userDto.getEmail());
    
    log.debug("userDto : " + userDto);
    log.debug("user : " + user);

    
    UserBuddy userToUpdate = userBuddyRepository.getOne(user.getId());
    
    log.debug("userToUpdate : " + userToUpdate);

    
    userToUpdate.setLastName(userDto.getLastName());
    userToUpdate.setFirstName(userDto.getFirstName());
    userToUpdate.setBirthdate(userDto.getBirthdate());
    userToUpdate.setAddress(userDto.getAddress());
    userToUpdate.setZip(userDto.getZip());
    userToUpdate.setCity(userDto.getCity());
    userToUpdate.setPhone(userDto.getPhone());

    UserBuddy userBuddy = userBuddyRepository.save(userToUpdate);
    
    return userBuddy;
  }

}
