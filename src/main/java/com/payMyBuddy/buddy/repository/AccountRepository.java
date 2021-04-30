package com.payMyBuddy.buddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payMyBuddy.buddy.model.Account;
import com.payMyBuddy.buddy.model.UserBuddy;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  Account findByUserBuddy(UserBuddy user);

}
