package com.tesi.datamasking.config;

import com.github.javafaker.Faker;
import com.tesi.datamasking.core.DataMaskingFacade;
import com.tesi.datamasking.data.db.customers.CustomersRepository;
import com.tesi.datamasking.data.db.employees.EmployeesRepository;
import com.tesi.datamasking.data.db.payslips.PayslipsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class DataMaskingRestConfig {

  @Bean
  public DataMaskingFacade dataMaskingFacade(EmployeesRepository employeesRepository,
      PayslipsRepository payslipsRepository,
      CustomersRepository customersRepository,
      Faker faker) {
    return new DataMaskingFacade(employeesRepository, payslipsRepository, customersRepository, faker);
  }

  @Bean
  public Faker faker() {
    return new Faker(new Locale("it"));
  }

}
