
package com.payMyBuddy.buddy.model;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
@Entity
@Table(name = "user_buddy", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserBuddy {

  /**
   * user Id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  /**
   * user email
   */
  @Column(nullable = false,unique =true)
  @NotNull
  private String email;
  
  /**
   * user password
   */
 @NotNull
  private String password;
  
 /**
  * user lastName
  */
  @Column(name = "last_name")
  private String lastName;
  
  /**
   * user firstName
   */
  @Column(name = "first_name")
  private String firstName;
  
  /**
   * user birthdate
   */
  private String birthdate;
  
  /**
   * user address
   */
  private String address;
  
  /**
   * user zip
   */
  private int zip;
  
  /**
   * user city
   */
  private String city;
  
  /**
   * user phone
   */
  private String phone;
  
  /**
   * User roles
   */
  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "user_id"),
      inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "role_id"))
 private Collection<Role> roles;
  
  /**
   * user bankAccount
   */
  @OneToOne
  @JoinColumn(name = "bank_account_id")
  private BankAccount bankAccount;

  /**
   * user contacts
   */
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "connexion", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "user_associate_id", referencedColumnName = "user_id"))
  private Set<UserBuddy> contacts;
  

};
