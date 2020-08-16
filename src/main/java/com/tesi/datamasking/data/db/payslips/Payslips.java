package com.tesi.datamasking.data.db.payslips;

import com.tesi.datamasking.context.DataCrypt;
import com.tesi.datamasking.data.db.employees.Employees;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "payslips")
public class Payslips {

  @EmbeddedId
  public PayslipKey key;

  @MapsId("employeeCode")
  @ManyToOne
  @JoinColumn(name = "employee_code")
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

  @DataCrypt(dataType = DataCrypt.DataType.AMOUNT)
  public BigDecimal amount;

}
