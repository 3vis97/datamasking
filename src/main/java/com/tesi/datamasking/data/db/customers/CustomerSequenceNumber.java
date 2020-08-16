package com.tesi.datamasking.data.db.customers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CustomerSequenceNumber {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
}
