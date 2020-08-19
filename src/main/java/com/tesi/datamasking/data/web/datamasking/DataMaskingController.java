package com.tesi.datamasking.data.web.datamasking;

import com.google.common.base.Stopwatch;
import com.tesi.datamasking.core.DataMaskingFacade;
import com.tesi.datamasking.data.dto.GenericRestResponse;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@RestController
public class DataMaskingController {

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

  @GetMapping("dataMasking/keepAlive")
  GenericRestResponse keepAlive() {
    LOGGER.info("call keepAlive");
    GenericRestResponse restResponse = new GenericRestResponse();
    restResponse.success = true;
    restResponse.details = "The application is up and running!";
    return restResponse;
  }
}
