package com.tesi.datamasking.data.web.customer;

import com.tesi.datamasking.algorithm.fpe.FPE_Impl;
import com.tesi.datamasking.data.db.customer.Customer;
import com.tesi.datamasking.data.db.customer.CustomerRepository;
import com.tesi.datamasking.data.dto.GenericRestResponse;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

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

  @PostMapping("dataMasking/maskData")
  GenericRestResponse maskData(@RequestBody
      PseudonymizationSetup pseudonymizationSetup) {
    GenericRestResponse restResponse = new GenericRestResponse();

    try {
      List<Customer> allCustomers = repository.findAll();
      FPE_Impl fpe = new FPE_Impl(KEY_1,KEY_2);
      fpe.useCustom();
      for (Customer customer : allCustomers) {
        fpe.cryptClass(customer, Arrays.asList(pseudonymizationSetup.fieldsToCrypt));
        repository.save(customer);
      }
      restResponse.success = true;
    }
    catch (Exception e) {
      restResponse.success = false;
      restResponse.error = e.getMessage();
    }
    return restResponse;
  }

}
