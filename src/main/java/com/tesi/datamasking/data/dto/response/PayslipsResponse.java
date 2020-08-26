package com.tesi.datamasking.data.dto.response;

import com.tesi.datamasking.data.db.payslips.Payslips;

import java.util.List;

public class PayslipsResponse extends GenericRestResponse {

  public List<Payslips> employeesList;

  public Payslips employees;
}
