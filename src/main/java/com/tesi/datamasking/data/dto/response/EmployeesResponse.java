package com.tesi.datamasking.data.dto.response;

import com.tesi.datamasking.data.db.employees.EmployeesDto;

import java.util.List;

public class EmployeesResponse extends GenericRestResponse {

  public EmployeesDto employee;

  public List<EmployeesDto> employeesDtoList;
}
