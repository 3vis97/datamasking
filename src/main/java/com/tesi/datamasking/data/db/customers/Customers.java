package com.tesi.datamasking.data.db.customers;

import com.tesi.datamasking.data.db.employees.Employees;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customers {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  public String companyName;

  public String vatNumber;

  public String phone;

  public String address;

  public String city;

  public Integer zipCode;

  @OneToMany(mappedBy = "customers")
  private List<Employees> employeesList;

  public List<Employees> getEmployeesList() {
    return employeesList;
  }

  public void setEmployeesList(List<Employees> employeesList) {
    this.employeesList = employeesList;
  }

}
