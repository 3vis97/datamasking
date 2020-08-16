package com.tesi.datamasking.data.db.employees;

import com.tesi.datamasking.data.db.payslips.PayslipKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeesRepository extends JpaRepository<Employees, PayslipKey> {
}
