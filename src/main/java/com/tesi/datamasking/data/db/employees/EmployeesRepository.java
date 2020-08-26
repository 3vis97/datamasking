package com.tesi.datamasking.data.db.employees;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeesRepository extends JpaRepository<Employees, String>, CustomEmployeesRepository {

  List<Employees> findByFirstNameAndLastName(String firstName,
      String lastName);

  List<Employees> findByCustomers_CustomerCode(String customerCode);

}
