package com.tesi.datamasking.data.db.employees;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface EmployeesRepository extends JpaRepository<Employees, String>, CustomEmployeesRepository {

  List<Employees> findByFirstNameAndLastName(String firstName,
      String lastName);

  List<Employees> findByCustomers_CustomerCode(String customerCode);

}
