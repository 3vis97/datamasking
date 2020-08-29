package com.tesi.datamasking.web;

import com.google.common.base.Stopwatch;
import com.tesi.datamasking.core.DataMaskingFacade;
import com.tesi.datamasking.data.db.payslips.PayslipKey;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import com.tesi.datamasking.data.dto.response.GenericRestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataMaskingController extends CoreController {

  private final DataMaskingFacade dataMaskingFacade;

  private static final Logger LOGGER = LoggerFactory.getLogger(DataMaskingController.class);

  @Autowired
  public DataMaskingController(DataMaskingFacade dataMaskingFacade) {
    this.dataMaskingFacade = dataMaskingFacade;
  }

  @DeleteMapping("dataMasking")
  GenericRestResponse deleteAll() {
    LOGGER.info("call deleteAll function");
    GenericRestResponse restResponse = new GenericRestResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      LOGGER.info("starting deleting payslips...");
      dataMaskingFacade.deleteAllPayslips();
      LOGGER.info("...complete!");
      LOGGER.info("starting deleting employees...");
      dataMaskingFacade.deleteAllEmployees();
      LOGGER.info("...complete!");
      LOGGER.info("starting deleting customers...");
      dataMaskingFacade.deleteAllCustomers();
      LOGGER.info("...complete!");
      stopwatch.stop();
      restResponse.details = formatPattern("Delete ALL", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @DeleteMapping("dataMasking/customer")
  GenericRestResponse deleteAllCustomers() {
    GenericRestResponse restResponse = new GenericRestResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.deleteAllEmployees();
      stopwatch.stop();
      restResponse.details = formatPattern("Delete CUSTOMER", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/addCustomers/{customers}/{batchSize}")
  GenericRestResponse populateCustomers(@PathVariable String customers) {
    GenericRestResponse restResponse = new GenericRestResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade
          .insertInBatchMode(Long.parseLong(customers));
      stopwatch.stop();
      restResponse.details = formatPattern("Populate CUSTOMERS", stopwatch);

    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/{customers}/{employees}/{payslip}")
  GenericRestResponse populateData(@PathVariable String customers,
      @PathVariable String employees,
      @PathVariable String payslip) {
    GenericRestResponse restResponse = new GenericRestResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade
          .populateRandomData(Long.parseLong(customers), Long.parseLong(employees), Integer.parseInt(payslip));
      stopwatch.stop();
      restResponse.details = formatPattern("Populate ALL", stopwatch);

    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/payslip/cryptAll")
  GenericRestResponse cryptPayslipData(@RequestBody
      PseudonymizationSetup pseudonymizationSetup) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.cryptAllPayslips(pseudonymizationSetup);
      stopwatch.stop();
      restResponse.details = formatPattern("Crypt ALL PAYSLIP", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/employee/update/{employeeCode}")
  GenericRestResponse updateSingleEmployee(
      @PathVariable String employeeCode) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.performVoidEmployeeUpdate(employeeCode);
      stopwatch.stop();
      restResponse.details = formatPattern("UPDATE SINGLE EMPLOYEE", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/employee/crypt/{employeeCode}")
  GenericRestResponse cryptSingleEmployee(
      @RequestBody PseudonymizationSetup pseudonymizationSetup,
      @PathVariable String employeeCode) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.cryptSingleEmployee(pseudonymizationSetup, employeeCode);
      stopwatch.stop();
      restResponse.details = formatPattern("Crypt SINGLE EMPLOYEE", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/employee/decrypt/{employeeCode}")
  GenericRestResponse decryptSingleEmployee(
      @RequestBody PseudonymizationSetup pseudonymizationSetup,
      @PathVariable String employeeCode) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.decryptSingleEmployee(pseudonymizationSetup, employeeCode);
      stopwatch.stop();
      restResponse.details = formatPattern("Decrypt SINGLE EMPLOYEE", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/payslip/crypt/{employeeCode}/{month}/{year}")
  GenericRestResponse cryptSinglePayslip(
      @RequestBody PseudonymizationSetup pseudonymizationSetup,
      @PathVariable String employeeCode,
      @PathVariable String month,
      @PathVariable String year) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.cryptSinglePayslip(pseudonymizationSetup,
          new PayslipKey(employeeCode, Integer.parseInt(month), Integer.parseInt(year)));
      stopwatch.stop();
      restResponse.details = formatPattern("Crypt SINGLE PAYSLIP", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/payslip/decrypt/{employeeCode}/{month}/{year}")
  GenericRestResponse decryptSinglePayslip(
      @RequestBody PseudonymizationSetup pseudonymizationSetup,
      @PathVariable String employeeCode,
      @PathVariable String month,
      @PathVariable String year) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.decryptSinglePayslip(pseudonymizationSetup,
          new PayslipKey(employeeCode, Integer.parseInt(month), Integer.parseInt(year)));
      stopwatch.stop();
      restResponse.details = formatPattern("Decrypt SINGLE PAYSLIP", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/payslip/decryptAll")
  GenericRestResponse decryptPayslipData(@RequestBody
      PseudonymizationSetup pseudonymizationSetup) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.decryptAllPayslips(pseudonymizationSetup);
      stopwatch.stop();
      restResponse.details = formatPattern("Decrypt ALL PAYSLIPS", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/employee/cryptAll")
  GenericRestResponse cryptEmployeeData(@RequestBody
      PseudonymizationSetup pseudonymizationSetup) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.cryptAllEmployees(pseudonymizationSetup);
      stopwatch.stop();
      restResponse.details = formatPattern("Crypt ALL EMPLOYEES", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.toString();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/employee/decryptAll")
  GenericRestResponse decryptEmployeeData(@RequestBody
      PseudonymizationSetup pseudonymizationSetup) {
    GenericRestResponse restResponse = new GenericRestResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.decryptAllEmployees(pseudonymizationSetup);
      stopwatch.stop();
      restResponse.details = formatPattern("Decrypt ALL EMPLOYEES", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.toString();
    }
    return restResponse;
  }

  @GetMapping("dataMasking/keepAlive")
  GenericRestResponse keepAlive() {
    LOGGER.info("call keepAlive");
    GenericRestResponse restResponse = new GenericRestResponse();
    restResponse.success = true;
    restResponse.details = "The application is up and running!";
    return restResponse;
  }

}
