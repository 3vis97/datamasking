package com.tesi.datamasking.web;

import com.google.common.base.Stopwatch;
import com.tesi.datamasking.core.TestFacade;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import com.tesi.datamasking.data.dto.response.GenericRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController extends CoreController {

  private final TestFacade testFacade;

  @Autowired
  public TestController(TestFacade testFacade) {
    this.testFacade = testFacade;
  }

  @PostMapping("test/Amounts/{amounts}")
  GenericRestResponse populateAmounts(@PathVariable String amounts) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      testFacade.populateRandomAmounts(Integer.parseInt(amounts));
      stopwatch.stop();
      restResponse.details = formatPattern("Populate random amounts", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("test/cryptAmounts")
  GenericRestResponse cryptAllAmounts(@RequestBody
      PseudonymizationSetup pseudonymizationSetup) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      testFacade.cryptAllAmounts(pseudonymizationSetup);
      stopwatch.stop();
      restResponse.details = formatPattern("Populate random amounts", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("test/cryptAmount/{code}")
  GenericRestResponse cryptSingleAmount(@PathVariable String code,
      @RequestBody
          PseudonymizationSetup pseudonymizationSetup) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      testFacade.cryptAmount(code, pseudonymizationSetup);
      stopwatch.stop();
      restResponse.details = formatPattern("Crypt single amount", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("test/decryptAmount/{code}")
  GenericRestResponse decryptAllAmounts(@PathVariable String code,
      @RequestBody
          PseudonymizationSetup pseudonymizationSetup) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      testFacade.decryptAmount(code, pseudonymizationSetup);
      stopwatch.stop();
      restResponse.details = formatPattern("Decrypt single amount", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("test/decryptAmounts")
  GenericRestResponse decryptAllAmounts(@RequestBody
      PseudonymizationSetup pseudonymizationSetup) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      testFacade.decryptAllAmounts(pseudonymizationSetup);
      stopwatch.stop();
      restResponse.details = formatPattern("Populate random amounts", stopwatch);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

}
