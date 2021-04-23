package com.payMyBuddy.buddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payMyBuddy.buddy.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{


}
