package com.payMyBuddy.buddy.configuration;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.payMyBuddy.buddy.model.Role;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService{
  
  @Autowired
  UserBuddyRepository userBuddyRepository;

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

}
