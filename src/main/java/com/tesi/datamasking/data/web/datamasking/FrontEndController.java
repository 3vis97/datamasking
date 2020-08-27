package com.tesi.datamasking.data.web.datamasking;

import com.google.common.base.Stopwatch;
import com.tesi.datamasking.core.DataMaskingFacade;
import com.tesi.datamasking.data.db.employees.Employees;
import com.tesi.datamasking.data.db.payslips.Payslips;
import com.tesi.datamasking.data.dto.response.EmployeesResponse;
import com.tesi.datamasking.data.dto.response.PayslipsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class FrontEndController extends CoreController {

  private final DataMaskingFacade dataMaskingFacade;

  private static final Logger LOGGER = LoggerFactory.getLogger(FrontEndController.class);

  private final int MAX_DIMENSION_FOR_OUTPUT = 1000;

  @Autowired
  public FrontEndController(DataMaskingFacade dataMaskingFacade) {
    this.dataMaskingFacade = dataMaskingFacade;
  }

  @GetMapping("front/getPayslip/{code}/{month}/{year}")
  PayslipsResponse getPayslip(@PathVariable String code,
      @PathVariable String month,
      @PathVariable String year) {
    PayslipsResponse response = new PayslipsResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      response.payslip = dataMaskingFacade.getSinglePayslip(code, Integer.parseInt(month), Integer.parseInt(year));
      stopwatch.stop();
      response.details = formatPattern("GET PAYSLIP given CODE, MONTH, YEAR", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }

  @GetMapping("front/getPayslips")
  PayslipsResponse getPayslips() {
    PayslipsResponse response = new PayslipsResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      List<Payslips> payslipsList = dataMaskingFacade.getAllPayslips();
      stopwatch.stop();
      if (payslipsList.size() <= MAX_DIMENSION_FOR_OUTPUT)
        response.payslipList = payslipsList;
      response.size = payslipsList.size();
      response.details = formatPattern("GET ALL PAYSLIPS", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }

  @GetMapping("front/getPayslips/{code}")
  PayslipsResponse getPayslips(@PathVariable String code) {
    PayslipsResponse response = new PayslipsResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      List<Payslips> payslipsList = dataMaskingFacade.getPayslips(code);
      stopwatch.stop();
      if (payslipsList.size() <= MAX_DIMENSION_FOR_OUTPUT)
        response.payslipList = payslipsList;
      response.size = payslipsList.size();
      response.details = formatPattern("GET PAYSLIPS", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }

  @GetMapping("front/getPayslips/{code}/{amount}/{operator}")
  PayslipsResponse getPayslipGivenAmount(@PathVariable String code,
      @PathVariable BigDecimal amount,
      @PathVariable String operator) {
    PayslipsResponse response = new PayslipsResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      response.payslipList = dataMaskingFacade.getPayslipsGivenAmount(code, amount, operator);
      stopwatch.stop();
      response.size = response.payslipList.size();
      response.details = formatPattern("GET PAYSLIP given AMOUNT", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }

  @GetMapping("front/getEmployees")
  EmployeesResponse getEmployees() {
    EmployeesResponse response = new EmployeesResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      List<Employees> employeesList = dataMaskingFacade.getAllEmployees();
      stopwatch.stop();
      if (employeesList.size() <= MAX_DIMENSION_FOR_OUTPUT)
        response.employeesList = employeesList;
      response.size = employeesList.size();
      response.details = formatPattern("GET ALL EMPLOYEES", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }

  @GetMapping("front/getEmployee/{name}/{lastName}")
  EmployeesResponse getEmployeeGivenNameAndLastName(@PathVariable String name,
      @PathVariable String lastName) {
    EmployeesResponse response = new EmployeesResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      response.employeesList = dataMaskingFacade.getEmployeeByFirstNameAndLastName(name, lastName);
      stopwatch.stop();
      response.size = response.employeesList.size();
      response.details = formatPattern("GET EMPLOYEE given FIRST NAME and LAST NAME", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }

  @GetMapping("front/getEmployeeMasked/{name}/{lastName}")
  EmployeesResponse getEmployeeMaskedGivenNameAndLastName(@PathVariable String name,
      @PathVariable String lastName) {
    EmployeesResponse response = new EmployeesResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      response.employeesDtoList = dataMaskingFacade.getEmployeeMaskedByFirstNameAndLastName(name, lastName);
      stopwatch.stop();
      response.size = response.employeesList.size();
      response.details = formatPattern("GET EMPLOYEE given FIRST NAME and LAST NAME", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }

  @GetMapping("front/getEmployees/{customerCode}")
  EmployeesResponse getEmployeeGivenCustomerCode(@PathVariable String customerCode) {
    EmployeesResponse response = new EmployeesResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      response.employeesList = dataMaskingFacade.getEmployeeByCustomerCode(customerCode);
      stopwatch.stop();
      response.size = response.employeesList.size();
      response.details = formatPattern("GET EMPLOYEES given CUSTOMER_CODE", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }
}
