package com.payMyBuddy.buddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payMyBuddy.buddy.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleServiceI{

  @Autowired
  RoleRepository roleRepository;

}
