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
import javax.validation.constraints.Size;

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
  @Size(max = 50, message = "Email must be less than 50 characters")
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
  @Size(max = 25, message = "Last name must be less than 25 characters")
  private String lastName;
  
  /**
   * user firstName
   */
  @Column(name = "first_name")
  @Size(max = 25, message = "First name must be less than 25 characters")
  private String firstName;
  
  /**
   * user birthdate
   */
  @Size(max = 12, message = "Birthdate must be less than 12 characters")
  private String birthdate;
  
  /**
   * user address
   */
  @Size(max = 100, message = "Address must be less than 100 characters")
  private String address;
  
  /**
   * user zip
   */
  private int zip;
  
  /**
   * user city
   */
  @Size(max = 50, message = "City must be less than 50 characters")
  private String city;
  
  /**
   * user phone
   */
  @Size(max = 12, message = "Phone must be less than 12 characters")
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
  @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
  @JoinTable(name = "connexion", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "user_associate_id", referencedColumnName = "user_id"))
  private Set<UserBuddy> contacts;
  
  /**
   * User active / for unsuscribe
   */
  private boolean active = true;
  

};
