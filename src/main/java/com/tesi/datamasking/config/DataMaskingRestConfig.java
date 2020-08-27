package com.tesi.datamasking.config;

import com.github.javafaker.Faker;
import com.tesi.datamasking.algorithm.CryptDecrypt;
import com.tesi.datamasking.core.DataMaskingFacade;
import com.tesi.datamasking.core.TestFacade;
import com.tesi.datamasking.data.db.DataEntityMapper;
import com.tesi.datamasking.data.db.amounts.AmountsRepository;
import com.tesi.datamasking.data.db.customers.CustomersRepository;
import com.tesi.datamasking.data.db.employees.EmployeesRepository;
import com.tesi.datamasking.data.db.payslips.PayslipsRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

@Configuration
public class DataMaskingRestConfig {

  @Bean
  public DataMaskingFacade dataMaskingFacade(EmployeesRepository employeesRepository,
      PayslipsRepository payslipsRepository,
      CustomersRepository customersRepository,
      Faker faker,
      CryptDecrypt cryptDecrypt,
      DataEntityMapper mapper) {
    return new DataMaskingFacade(employeesRepository, payslipsRepository, customersRepository, faker, cryptDecrypt,
        mapper);
  }

  @Bean
  public Faker faker() {
    return new Faker(new Locale("it"));
  }

  @Bean
  public CryptDecrypt cryptDecrypt(@Value("${secret.key.one}") String KEY_1,
      @Value("${secret.key.two}") String KEY_2)
      throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException {
    return new CryptDecrypt(KEY_1, KEY_2);
  }

  @Bean
  public TestFacade testFacade(AmountsRepository amountsRepository,
      Faker faker,
      CryptDecrypt cryptDecrypt) {
    return new TestFacade(amountsRepository, faker, cryptDecrypt);
  }

  @Bean
  public DataEntityMapper mapper() {
    return Mappers.getMapper(DataEntityMapper.class);
  }

}
