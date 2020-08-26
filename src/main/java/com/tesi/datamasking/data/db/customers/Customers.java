package com.tesi.datamasking.data.db.customers;

import com.tesi.datamasking.data.db.employees.Employees;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customers {

  @Id
  public String customerCode;

  public String companyName;

  public String vatNumber;

  public String phone;

  public String address;

  public String city;

  public Integer zipCode;

  @OneToMany(mappedBy = "customers")
  private List<Employees> employeesList;

}
