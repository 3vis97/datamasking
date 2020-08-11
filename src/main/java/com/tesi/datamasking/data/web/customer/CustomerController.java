package com.tesi.datamasking.data.web.customer;

import com.github.javafaker.Faker;
import com.google.common.base.Stopwatch;
import com.tesi.datamasking.algorithm.fpe.FPE_Impl;
import com.tesi.datamasking.data.db.dipendenti.Dipendenti;
import com.tesi.datamasking.data.db.dipendenti.DipendentiRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@RestController
public class CustomerController {

  private final DipendentiRepository repository;

  @Value("${secret.key.one}")
  private String KEY_1;

  @Value("${secret.key.two}")
  private String KEY_2;

  @Autowired
  public CustomerController (DipendentiRepository customerRepository) {
    this.repository = customerRepository;
  }

  @GetMapping("dataMasking/customer")
  List<Dipendenti> all() {
    return repository.findAll();
  }

  @DeleteMapping("dataMasking/customer")
  GenericRestResponse deleteAll() {
    GenericRestResponse restResponse = new GenericRestResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      repository.deleteAll();
      stopwatch.stop();
      long sec = stopwatch.elapsed(TimeUnit.SECONDS);
      restResponse.success = true;
      restResponse.details = MessageFormat.format("Delete completed in {0} seconds", sec);
    } catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

  @PostMapping("dataMasking/customer/{records}")
  GenericRestResponse populateData(@PathVariable String records) {
    GenericRestResponse restResponse = new GenericRestResponse();
    try {
      Stopwatch stopwatch = Stopwatch.createStarted();
      for (int i=0; i < Integer.parseInt(records); i++) {
        repository.save(generateRandomCustomer());
      }
      stopwatch.stop();
      long sec = stopwatch.elapsed(TimeUnit.SECONDS);
      restResponse.details = MessageFormat.format("All {0} inserts completed in {1} seconds.", records, sec);
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
      List<Dipendenti> allCustomers = repository.findAll();
      FPE_Impl fpe = new FPE_Impl(KEY_1,KEY_2);
      Stopwatch stopwatch = Stopwatch.createStarted();
      for (Dipendenti customer : allCustomers) {
        fpe.cryptClass(customer, Arrays.asList(pseudonymizationSetup.fields));
        repository.save(customer);
      }
      stopwatch.stop();
      long sec = stopwatch.elapsed(TimeUnit.SECONDS);
      restResponse.success = true;
      restResponse.details = "Crypt completed in " + sec + " seconds";
    }
    catch (Exception e) {
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
      List<Dipendenti> allCustomers = repository.findAll();
      FPE_Impl fpe = new FPE_Impl(KEY_1,KEY_2);
      Stopwatch stopwatch = Stopwatch.createStarted();
      for (Dipendenti customer : allCustomers) {
        fpe.decryptClass(customer, Arrays.asList(pseudonymizationSetup.fields));
        repository.save(customer);
      }
      stopwatch.stop();
      long sec = stopwatch.elapsed(TimeUnit.SECONDS);
      restResponse.success = true;
      restResponse.details = "Decrypt completed in " + sec + " seconds";
    }
    catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }


  private Dipendenti generateRandomCustomer() {
    Faker faker = new Faker(new Locale("it"));
    Dipendenti customer = new Dipendenti();

    customer.nome = faker.name().firstName();
    customer.cognome = faker.name().lastName();
    customer.citta = faker.address().city();
    customer.stato = faker.address().state();
    customer.indirizzo = faker.address().streetAddress();
    customer.email = faker.internet().emailAddress().replace(" ","");
    customer.telefono = faker.phoneNumber().phoneNumber();
    customer.cap = Integer.parseInt(faker.address().zipCode());

    return customer;
  }

}
