package com.tesi.datamasking.data.web.customer;

import com.tesi.datamasking.data.db.customer.Customer;
import com.tesi.datamasking.data.db.customer.CustomerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

  private final CustomerRepository repository;

  CustomerController (CustomerRepository customerRepository) {
    this.repository = customerRepository;
  }

  @GetMapping("dataMasking/customer")
  List<Customer> all() {
    return repository.findAll();
  }

}
