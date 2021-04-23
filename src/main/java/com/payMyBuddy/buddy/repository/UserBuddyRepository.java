package com.payMyBuddy.buddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payMyBuddy.buddy.model.UserBuddy;

@Repository
public interface UserBuddyRepository extends JpaRepository<UserBuddy, Long> {

  UserBuddy findByemail(String email);

}
