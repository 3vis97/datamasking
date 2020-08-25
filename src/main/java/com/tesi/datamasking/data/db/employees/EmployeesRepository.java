package com.tesi.datamasking.data.db.employees;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeesRepository extends JpaRepository<Employees, String>, CustomEmployeesRepository {
}
