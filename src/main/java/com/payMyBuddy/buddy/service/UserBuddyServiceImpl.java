package com.payMyBuddy.buddy.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    UserBuddy user = new UserBuddy();
    
    user.setEmail(userRegistrationDto.getEmail());
    user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
    log.debug("mail : " + userRegistrationDto.getEmail());
    if(userRegistrationDto.getEmail().equalsIgnoreCase("admin4@paymybuddy.com")) {
      user.setRoles(Arrays.asList(new Role("ROLE_ADMIN")));
    }else {
      user.setRoles(Arrays.asList(new Role("ROLE_USER")));
    }
    UserBuddy userBuddy = userBuddyRepository.save(user);
    accountServiceI.save(userBuddy);
    
    return userBuddy;
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
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserBuddy user = userBuddyRepository.findByemail(username);
    if (user == null) {
      throw new UsernameNotFoundException("Invalid email or password");
    }
    return new org.springframework.security.core.userdetails.User(user.getEmail(),
        user.getPassword(), mapRolesAuthorities(user.getRoles()));
  }

  private Collection<? extends GrantedAuthority> mapRolesAuthorities(Collection<Role> roles) {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }

  @Override
  public boolean existsUserBuddyByEmail(String email) {

    return userBuddyRepository.existsUserBuddyByEmail(email);
  }

  @Override
  public UserBuddy save(UserProfileDto userDto) {
    UserBuddy user = new UserBuddy();
    
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = authentication.getName();
    user = userBuddyRepository.findByemail(username);
    
    UserBuddy userToUpdate = userBuddyRepository.getOne(user.getId());
    
    
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
