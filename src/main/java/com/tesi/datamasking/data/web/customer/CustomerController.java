package com.tesi.datamasking.data.web.customer;

import com.google.common.base.Stopwatch;
import com.tesi.datamasking.core.DataMaskingFacade;
import com.tesi.datamasking.data.db.dipendenti.Dipendenti;
import com.tesi.datamasking.data.dto.GenericRestResponse;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class CustomerController {

  private final DataMaskingFacade dataMaskingFacade;

  @Value("${secret.key.one}")
  private String KEY_1;

  @Value("${secret.key.two}")
  private String KEY_2;

  @Autowired
  public CustomerController(DataMaskingFacade dataMaskingFacade) {
    this.dataMaskingFacade = dataMaskingFacade;
  }

  @GetMapping("dataMasking/customer")
  List<Dipendenti> all() {
    return dataMaskingFacade.getAllDipendenti();
  }

  @DeleteMapping("dataMasking/customer")
  GenericRestResponse deleteAll() {
    GenericRestResponse restResponse = new GenericRestResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.deleteAllDipendenti();
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

    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/customer/cryptData")
  GenericRestResponse cryptData(@RequestBody
      PseudonymizationSetup pseudonymizationSetup) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.cryptAllDipendenti(pseudonymizationSetup, KEY_1, KEY_2);
      stopwatch.stop();
      restResponse.details = MessageFormat
          .format("Crypt completed in {0} seconds.", stopwatch.elapsed(TimeUnit.SECONDS));
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/customer/decryptData")
  GenericRestResponse decryptData(@RequestBody
      PseudonymizationSetup pseudonymizationSetup) {
    GenericRestResponse restResponse = new GenericRestResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      dataMaskingFacade.decryptAllDipendenti(pseudonymizationSetup, KEY_1, KEY_2);
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
