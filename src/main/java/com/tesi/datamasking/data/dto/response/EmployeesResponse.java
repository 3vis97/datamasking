package com.tesi.datamasking.data.dto.response;

import com.tesi.datamasking.data.db.employees.Employees;

import java.util.List;

public class EmployeesResponse extends GenericRestResponse {

  public List<Employees> employeesList;

  public Employees employees;
}
