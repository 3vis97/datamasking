package com.tesi.datamasking.config;

import com.github.javafaker.Faker;
import com.tesi.datamasking.core.DataMaskingFacade;
import com.tesi.datamasking.data.db.cedolini.CedoliniLogRepository;
import com.tesi.datamasking.data.db.clienti.ClientiRepository;
import com.tesi.datamasking.data.db.dipendenti.DipendentiRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class DataMaskingRestConfig {

  @Bean
  public DataMaskingFacade dataMaskingFacade(DipendentiRepository dipendentiRepository,
      CedoliniLogRepository cedoliniLogRepository,
      ClientiRepository clientiRepository,
      Faker faker) {
    return new DataMaskingFacade(dipendentiRepository, cedoliniLogRepository, clientiRepository, faker);
  }

  @Bean
  public Faker faker() {
    return new Faker(new Locale("it"));
  }

}
