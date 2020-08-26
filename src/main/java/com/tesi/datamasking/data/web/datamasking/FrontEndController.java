package com.tesi.datamasking.data.web.datamasking;

import com.google.common.base.Stopwatch;
import com.tesi.datamasking.core.DataMaskingFacade;
import com.tesi.datamasking.data.dto.response.PayslipsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

public class FrontEndController extends CoreController {

  private final DataMaskingFacade dataMaskingFacade;

  private static final Logger LOGGER = LoggerFactory.getLogger(DataMaskingController.class);

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
      response.employees = dataMaskingFacade.getSinglePayslip(code, Integer.parseInt(month), Integer.parseInt(year));
      stopwatch.stop();
      response.details = formatPattern("GET PAYSLIP given CODE, MONTH, YEAR", stopwatch);
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
      response.employeesList = dataMaskingFacade.getPayslips(code);
      stopwatch.stop();
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
      response.employeesList = dataMaskingFacade.getPayslipsGivenAmount(code, amount, operator);
      stopwatch.stop();
      response.details = formatPattern("GET PAYSLIP given AMOUNT", stopwatch);
    } catch (Exception e) {
      response.success = false;
      response.error = e.getMessage();
    }
    return response;
  }
}
