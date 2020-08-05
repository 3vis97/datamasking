package com.tesi.datamasking.config;

import com.github.javafaker.Faker;
import com.tesi.datamasking.data.db.customer.Customer;
import com.tesi.datamasking.data.db.customer.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(CustomerRepository repository) {
    return args -> {
      log.info("Delete all previous entries... ");
      repository.deleteAll();
      log.info("Load data... ");
      for (int i=0; i < 10; i++) {
        repository.save(generateRandomCustomer());
      }
      log.info("...done! ");
    };
  }


  private Customer generateRandomCustomer() {
    Faker faker = new Faker(new Locale("it"));
    Customer customer = new Customer();

    customer.firstName = faker.name().firstName();
    customer.lastName = faker.name().lastName();
    customer.city = faker.address().city();
    customer.state = faker.address().state();
    customer.street = faker.address().streetAddress();
    customer.email = faker.internet().emailAddress();
    customer.phone = faker.phoneNumber().phoneNumber();
    customer.zipCode = faker.address().zipCode();

    return customer;
  }
}
