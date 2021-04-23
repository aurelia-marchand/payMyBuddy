package com.payMyBuddy.buddy.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.payMyBuddy.buddy.dto.UserRegistrationDto;
import com.payMyBuddy.buddy.model.Role;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;

@Service
public class UserBuddyServiceImpl implements UserBuddyServiceI{

  @Autowired
  private UserBuddyRepository userBuddyRepository;
  
  @Autowired
  private PasswordEncoder passwordEncoder;
  

  @Override
  public UserBuddy save(UserRegistrationDto userRegistrationDto) {
    UserBuddy user = new UserBuddy();
    user.setEmail(userRegistrationDto.getEmail());
    user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
    user.setRoles(Arrays.asList(new Role("ROLE_USER")));
    
    return userBuddyRepository.save(user);
  }
  
  @Override
  public List<UserBuddy> findAll() {
    // TODO Auto-generated method stub
    return userBuddyRepository.findAll();
  }
  
  @Override
  public UserBuddy findOne(String email) {
    return userBuddyRepository.findByemail(email);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserBuddy user = userBuddyRepository.findByemail(username);
    if(user == null) {
      throw new UsernameNotFoundException("Invalid email or password");
    }
    
    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesAuthorities(user.getRoles()));
    
  }
  
  private Collection<? extends GrantedAuthority> mapRolesAuthorities(Collection<Role> roles) {
    
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    
   
  }

 
}
