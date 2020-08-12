package com.tesi.datamasking.data.web.datamasking;

import com.google.common.base.Stopwatch;
import com.tesi.datamasking.core.DataMaskingFacade;
import com.tesi.datamasking.data.dto.GenericRestResponse;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@RestController
public class DataMaskingController {

  private final DataMaskingFacade dataMaskingFacade;

  @Autowired
  public DataMaskingController(DataMaskingFacade dataMaskingFacade) {
    this.dataMaskingFacade = dataMaskingFacade;
  }

  @DeleteMapping("dataMasking")
  GenericRestResponse deleteAll() {
    GenericRestResponse restResponse = new GenericRestResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.deleteAllPayslips();
      dataMaskingFacade.deleteAllEmployees();
      dataMaskingFacade.deleteAllCustomers();
      stopwatch.stop();
      restResponse.details = MessageFormat
          .format("Delete completed in {0} seconds", stopwatch.elapsed(TimeUnit.SECONDS));
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
      restResponse.details = MessageFormat
          .format("Delete completed in {0} seconds", stopwatch.elapsed(TimeUnit.SECONDS));
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
      restResponse.details = MessageFormat
          .format("Populate completed in {0} seconds", stopwatch.elapsed(TimeUnit.SECONDS));

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
      restResponse.details = MessageFormat
          .format("Crypt completed in {0} seconds.", stopwatch.elapsed(TimeUnit.SECONDS));
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
      restResponse.details = MessageFormat
          .format("Crypt completed in {0} seconds.", stopwatch.elapsed(TimeUnit.SECONDS));
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
      restResponse.details = MessageFormat
          .format("Crypt completed in {0} seconds.", stopwatch.elapsed(TimeUnit.SECONDS));
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
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
      restResponse.details = MessageFormat
          .format("Decrypt completed in {0} seconds.", stopwatch.elapsed(TimeUnit.SECONDS));
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }
}
