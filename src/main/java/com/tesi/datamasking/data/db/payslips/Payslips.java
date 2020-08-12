package com.tesi.datamasking.data.db.payslips;

import com.tesi.datamasking.data.db.employees.Employees;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "payslips")
public class Payslips {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long id;

  @ManyToOne
  @JoinColumn(name = "employee_id")
  public Employees employees;

  public String employeeJob;

  public String column1;

  public String column2;

  public String column3;

  public String column4;

  public String column5;

  public String column6;

  public String column7;

  public String column8;

  public String column9;

  public String column10;

  public Integer payslipMonth;

  public Integer payslipYear;

  public BigDecimal amount;

}
