package com.tesi.datamasking.data.db.employees;

import java.util.List;

public interface CustomEmployeesRepository {

  void insertWithBatchInsert(List<Employees> employeesList);

}
