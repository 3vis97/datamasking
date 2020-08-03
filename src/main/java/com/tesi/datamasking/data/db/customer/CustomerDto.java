package com.tesi.datamasking.data.db.customer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class CustomerDto {

  public Integer customerId;

  public String firstName;

  public String lastName;

  public String phone;

  public String email;

  public String street;

  public String city;

  public String state;

  public String zipCode;
}
