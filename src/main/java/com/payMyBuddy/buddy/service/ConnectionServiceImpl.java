package com.payMyBuddy.buddy.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.payMyBuddy.buddy.dto.UserConnectionDto;
import com.payMyBuddy.buddy.model.UserBuddy;
import com.payMyBuddy.buddy.repository.UserBuddyRepository;

@Service
public class ConnectionServiceImpl implements ConnectionServiceI{
  
  @Autowired
  UserBuddyRepository userBuddyRepository;

  /**
   * To add contact to userBuddy account
   */
  @Override
  public void add(UserConnectionDto userConnectionDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();  
    String username = authentication.getName();
    UserBuddy user = userBuddyRepository.findByemail(username);
    
    UserBuddy userToUpdate = userBuddyRepository.getOne(user.getId());
    Set<UserBuddy> contacts = userToUpdate.getContacts();
    
    UserBuddy userConnect = userBuddyRepository.findByemail(userConnectionDto.getEmail());
    contacts.add(userConnect);
    userToUpdate.setContacts(contacts);
    userBuddyRepository.save(userToUpdate);
    
  }

}
