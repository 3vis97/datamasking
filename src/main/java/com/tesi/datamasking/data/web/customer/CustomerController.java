package com.tesi.datamasking.data.web.customer;

import com.github.javafaker.Faker;
import com.google.common.base.Stopwatch;
import com.tesi.datamasking.algorithm.fpe.FPE_Impl;
import com.tesi.datamasking.data.db.customer.Customer;
import com.tesi.datamasking.data.db.customer.CustomerRepository;
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

  private final CustomerRepository repository;

  @Value("${secret.key.one}")
  private String KEY_1;

  @Value("${secret.key.two}")
  private String KEY_2;

  @Autowired
  public CustomerController (CustomerRepository customerRepository) {
    this.repository = customerRepository;
  }

  @GetMapping("dataMasking/customer")
  List<Customer> all() {
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
      List<Customer> allCustomers = repository.findAll();
      FPE_Impl fpe = new FPE_Impl(KEY_1,KEY_2);
      Stopwatch stopwatch = Stopwatch.createStarted();
      for (Customer customer : allCustomers) {
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
      List<Customer> allCustomers = repository.findAll();
      FPE_Impl fpe = new FPE_Impl(KEY_1,KEY_2);
      Stopwatch stopwatch = Stopwatch.createStarted();
      for (Customer customer : allCustomers) {
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


  private Customer generateRandomCustomer() {
    Faker faker = new Faker(new Locale("it"));
    Customer customer = new Customer();

    customer.firstName = faker.name().firstName();
    customer.lastName = faker.name().lastName();
    customer.city = faker.address().city();
    customer.state = faker.address().state();
    customer.street = faker.address().streetAddress();
    customer.email = faker.internet().emailAddress().replace(" ","");
    customer.phone = faker.phoneNumber().phoneNumber();
    customer.zipCode = faker.address().zipCode();

    return customer;
  }

}
