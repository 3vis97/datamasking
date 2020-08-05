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

  @DataCrypt(dataType = DataCrypt.DataType.FIRST_NAME)
  public String firstName;

  @DataCrypt(dataType = DataCrypt.DataType.LAST_NAME)
  public String lastName;

  public String phone;

  public String email;

  public String street;

  public String city;

  public String state;

  public String zipCode;
}
