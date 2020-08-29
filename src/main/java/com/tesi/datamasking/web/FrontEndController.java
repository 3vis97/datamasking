package com.tesi.datamasking.web;

import com.google.common.base.Stopwatch;
import com.tesi.datamasking.core.DataMaskingFacade;
import com.tesi.datamasking.data.db.employees.EmployeesDto;
import com.tesi.datamasking.data.db.payslips.PayslipsDto;
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

  //region QUERY PAYSLIPS - (A)
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

  @GetMapping("front/getPayslipMasked/{code}/{month}/{year}")
  PayslipsResponse getPayslipMasked(@PathVariable String code,
      @PathVariable String month,
      @PathVariable String year) {
    PayslipsResponse response = new PayslipsResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      response.payslip = dataMaskingFacade
          .getSingleMaskedPayslip(code, Integer.parseInt(month), Integer.parseInt(year));
      stopwatch.stop();
      response.details = formatPattern("GET PAYSLIP given CODE, MONTH, YEAR", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }
  //endregion

  //region QUERY PAYSLIPS - (B)
  @GetMapping("front/getPayslips/{code}")
  PayslipsResponse getPayslips(@PathVariable String code) {
    PayslipsResponse response = new PayslipsResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      List<PayslipsDto> payslipsList = dataMaskingFacade.getPayslips(code);
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

  @GetMapping("front/getPayslipsMasked/{code}")
  PayslipsResponse getPayslipsMasked(@PathVariable String code) {
    PayslipsResponse response = new PayslipsResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      List<PayslipsDto> payslipsList = dataMaskingFacade.getPayslipsMasked(code);
      stopwatch.stop();
      if (payslipsList.size() <= MAX_DIMENSION_FOR_OUTPUT)
        response.payslipList = payslipsList;
      response.size = payslipsList.size();
      response.details = formatPattern("GET PAYSLIPS MASKED", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }
  //endregion

  //region QUERY PAYSLIPS - (C)
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

  @GetMapping("front/getPayslipsMasked/{code}/{amount}/{operator}")
  PayslipsResponse getPayslipMaskedGivenAmount(@PathVariable String code,
      @PathVariable BigDecimal amount,
      @PathVariable String operator) {
    PayslipsResponse response = new PayslipsResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      response.payslipList = dataMaskingFacade.getPayslipsMaskedGivenAmount(code, amount, operator);
      stopwatch.stop();
      response.size = response.payslipList.size();
      response.details = formatPattern("GET PAYSLIP MASKED given AMOUNT", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }
  //endregion

  //region QUERY EMPLOYEES - (A)
  @GetMapping("front/getEmployee/{name}/{lastName}")
  EmployeesResponse getEmployeeGivenNameAndLastName(@PathVariable String name,
      @PathVariable String lastName) {
    EmployeesResponse response = new EmployeesResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      response.employeesDtoList = dataMaskingFacade.getEmployeeByFirstNameAndLastName(name, lastName);
      stopwatch.stop();
      response.size = response.employeesDtoList.size();
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
      response.size = response.employeesDtoList.size();
      response.details = formatPattern("GET EMPLOYEE MASKED given FIRST NAME and LAST NAME", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }
  //endregion

  //region QUERY EMPLOYEES - (B)
  @GetMapping("front/getEmployees/{customerCode}")
  EmployeesResponse getEmployeeGivenCustomerCode(@PathVariable String customerCode) {
    EmployeesResponse response = new EmployeesResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      response.employeesDtoList = dataMaskingFacade.getEmployeeByCustomerCode(customerCode);
      stopwatch.stop();
      response.size = response.employeesDtoList.size();
      response.details = formatPattern("GET EMPLOYEES given CUSTOMER_CODE", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }

  @GetMapping("front/getEmployeesMasked/{customerCode}")
  EmployeesResponse getEmployeeMaskedGivenCustomerCode(@PathVariable String customerCode) {
    EmployeesResponse response = new EmployeesResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      response.employeesDtoList = dataMaskingFacade.getEmployeeMaskedByCustomerCode(customerCode);
      stopwatch.stop();
      response.size = response.employeesDtoList.size();
      response.details = formatPattern("GET EMPLOYEES MASKED given CUSTOMER_CODE", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }
  //endregion


  /*
    -------------------- OTHER METHODS ------------------------
   */

  @GetMapping("front/getEmployees")
  EmployeesResponse getEmployees() {
    EmployeesResponse response = new EmployeesResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      List<EmployeesDto> employeesList = dataMaskingFacade.getEmployees();
      stopwatch.stop();
      if (employeesList.size() <= MAX_DIMENSION_FOR_OUTPUT)
        response.employeesDtoList = employeesList;
      response.size = employeesList.size();
      response.details = formatPattern("GET ALL EMPLOYEES", stopwatch);
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
      List<PayslipsDto> payslipsList = dataMaskingFacade.getAllPayslips();
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
}
