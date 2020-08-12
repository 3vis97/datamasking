package com.tesi.datamasking.data.db.employees;

import com.tesi.datamasking.context.DataCrypt;
import com.tesi.datamasking.data.db.customers.Customers;
import com.tesi.datamasking.data.db.payslips.Payslips;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @ManyToOne
  @JoinColumn(name = "customerId")
  public Customers customers;

  @DataCrypt
  public String firstName;

  @DataCrypt
  public String lastName;

  public String phone;

  @DataCrypt(dataType = DataCrypt.DataType.EMAIL)
  public String email;

  public String address;

  public String city;

  @DataCrypt(dataType = DataCrypt.DataType.NUMBER)
  public Integer zipCode;

  @OneToMany(mappedBy = "employees")
  private List<Payslips> payslips;

  public List<Payslips> getCedoliniLogs() {
    return payslips;
  }

  public void setCedoliniLogs(List<Payslips> payslips) {
    this.payslips = payslips;
  }
}
