package com.tesi.datamasking.data.db.payslips;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PayslipKey implements Serializable {

  public String employeeCode;

  public Integer payslipMonth;

  public Integer payslipYear;

  public PayslipKey() {
  }

  public PayslipKey(String employeeCode,
      Integer payslipMonth,
      Integer payslipYear) {
    this.employeeCode = employeeCode;
    this.payslipMonth = payslipMonth;
    this.payslipYear = payslipYear;
  }

}
