package com.payMyBuddy.buddy.service;

import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.UserBuddy;

public interface AccountServiceI {

  Account findByUserAccountId(UserBuddy user);

  String save(UserBuddy user);

 
}
