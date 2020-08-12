package com.tesi.datamasking.data.db.payslips;

import com.tesi.datamasking.context.DataCrypt;
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

  @DataCrypt(dataType = DataCrypt.DataType.LONG_STRING)
  public String column1;

  @DataCrypt(dataType = DataCrypt.DataType.LONG_STRING)
  public String column2;

  @DataCrypt(dataType = DataCrypt.DataType.LONG_STRING)
  public String column3;

  @DataCrypt(dataType = DataCrypt.DataType.LONG_STRING)
  public String column4;

  @DataCrypt(dataType = DataCrypt.DataType.LONG_STRING)
  public String column5;

  @DataCrypt(dataType = DataCrypt.DataType.LONG_STRING)
  public String column6;

  @DataCrypt(dataType = DataCrypt.DataType.LONG_STRING)
  public String column7;

  @DataCrypt(dataType = DataCrypt.DataType.LONG_STRING)
  public String column8;

  @DataCrypt(dataType = DataCrypt.DataType.LONG_STRING)
  public String column9;

  @DataCrypt(dataType = DataCrypt.DataType.LONG_STRING)
  public String column10;

  public Integer payslipMonth;

  public Integer payslipYear;

  @DataCrypt(dataType = DataCrypt.DataType.AMOUNT)
  public BigDecimal amount;

}
