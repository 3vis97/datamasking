package com.tesi.datamasking.data.db.employees;

import com.tesi.datamasking.context.DataCrypt;
import com.tesi.datamasking.data.db.customers.Customers;
import com.tesi.datamasking.data.db.payslips.Payslips;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employees {
  @Id
  public String employeeCode;

  @ManyToOne
  @JoinColumn(name = "customer_code")
  public Customers customers;

  @DataCrypt
  public String firstName;

  @DataCrypt
  public String lastName;

  @DataCrypt(dataType = DataCrypt.DataType.PHONE)
  public String phone;

  @DataCrypt(dataType = DataCrypt.DataType.EMAIL)
  public String email;

  @DataCrypt(dataType = DataCrypt.DataType.UNICODE)
  public String address;

  @DataCrypt(dataType = DataCrypt.DataType.UNICODE)
  public String city;

  public Integer zipCode;

  @OneToMany(mappedBy = "employees")
  private List<Payslips> payslips;
}
