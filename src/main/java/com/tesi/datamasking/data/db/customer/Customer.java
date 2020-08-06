package com.tesi.datamasking.data.db.customer;

import com.tesi.datamasking.context.DataCrypt;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="customer")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer customerId;

  @DataCrypt
  public String firstName;

  @DataCrypt
  public String lastName;

  public String phone;

  public String email;

  public String street;

  public String city;

  public String state;

  public String zipCode;
}
