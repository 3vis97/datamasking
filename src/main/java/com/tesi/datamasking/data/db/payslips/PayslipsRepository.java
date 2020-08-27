package com.tesi.datamasking.data.db.payslips;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Transactional
public interface PayslipsRepository extends JpaRepository<Payslips, PayslipKey>, CustomPayslipsRepository {

  List<Payslips> findByKeyEmployeeCode(String employeeCode);

  // >
  List<Payslips> findByKeyEmployeeCodeAndAmountGreaterThan(String employeeCode,
      BigDecimal amount);

  // >=
  List<Payslips> findByKeyEmployeeCodeAndAmountGreaterThanEqual(String employeeCode,
      BigDecimal amount);

  // <
  List<Payslips> findByKeyEmployeeCodeAndAmountLessThan(String employeeCode,
      BigDecimal amount);

  // <=
  List<Payslips> findByKeyEmployeeCodeAndAmountLessThanEqual(String employeeCode,
      BigDecimal amount);

  // =
  List<Payslips> findByKeyEmployeeCodeAndAmountIs(String employeeCode,
      BigDecimal amount);

}
