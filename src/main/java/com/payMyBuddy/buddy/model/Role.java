package com.payMyBuddy.buddy.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Role {
  
  public Role(String name) {
    super();
    this.name = name;
  }
  

  public Role() {
    super();
    // TODO Auto-generated constructor stub
  }


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long id;
  
  private String name;
  

}
