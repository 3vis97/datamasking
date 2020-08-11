package com.tesi.datamasking.core;

import com.github.javafaker.Faker;
import com.tesi.datamasking.data.db.cedolini.CedoliniLog;
import com.tesi.datamasking.data.db.cedolini.CedoliniLogRepository;
import com.tesi.datamasking.data.db.clienti.Clienti;
import com.tesi.datamasking.data.db.clienti.ClientiRepository;
import com.tesi.datamasking.data.db.dipendenti.Dipendenti;
import com.tesi.datamasking.data.db.dipendenti.DipendentiRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class DataMaskingFacade {

  private final DipendentiRepository dipendentiRepository;
  private final CedoliniLogRepository cedoliniLogRepository;
  private final ClientiRepository clientiRepository;

  @Autowired
  public DataMaskingFacade(DipendentiRepository dipendentiRepository, CedoliniLogRepository cedoliniLogRepository,
      ClientiRepository clientiRepository) {
    this.cedoliniLogRepository = cedoliniLogRepository;
    this.dipendentiRepository =dipendentiRepository;
    this.clientiRepository = clientiRepository;
  }

  public Dipendenti saveDipendente(Dipendenti dipendente) {
    return dipendentiRepository.save(dipendente);
  }

  public Clienti saveCliente(Clienti cliente) {
    return clientiRepository.save(cliente);
  }

  public List<Dipendenti> getAllDipendenti() {
    return dipendentiRepository.findAll();
  }

  public void deleteAllDipendenti() {
    dipendentiRepository.deleteAll();
  }

  public void populateRandomData(long customers, long employees, long payslip) {
      for (int i = 0; i < customers; i++) {
        Clienti clienteSaved = saveCliente(generateRandomCliente());
        Long idCliente = clienteSaved.id;
        for (int j = 0; j < employees; j++) {
          for (int z = 0; z < payslip; z++) {

          }
        }
      }
  }


  private CedoliniLog generateRandomCedolino(Long idDipendente, int mese, int anno, BigDecimal importo) {
    Faker faker = new Faker(new Locale("it"));
    CedoliniLog cedolinoLog = new CedoliniLog();
    int min = 1;
    int max = 10;

    cedolinoLog.anno = anno;
    cedolinoLog.mese = mese;
    cedolinoLog.idDipendente = idDipendente;
    cedolinoLog.colonna1 = randomValue(faker, min, max);
    cedolinoLog.colonna2 = randomValue(faker, min, max);
    cedolinoLog.colonna3 = randomValue(faker, min, max);
    cedolinoLog.colonna4 = randomValue(faker, min, max);
    cedolinoLog.colonna5 = randomValue(faker, min, max);
    cedolinoLog.colonna6 = randomValue(faker, min, max);
    cedolinoLog.colonna7 = randomValue(faker, min, max);
    cedolinoLog.colonna8 = randomValue(faker, min, max);
    cedolinoLog.colonna9 = randomValue(faker, min, max);
    cedolinoLog.colonna10 = randomValue(faker, min, max);
    cedolinoLog.importo = importo;

    return cedolinoLog;

  }

  private String randomValue(Faker faker, int min, int max){
    int value = getRandomNumber(min, max);
    switch (value) {
    case 1: return faker.finance().iban();
    case 2: return faker.business().creditCardNumber();
    case 3: return faker.book().author();
    case 4: return faker.address().city();
    case 5: return faker.company().profession();
    case 6: return faker.company().industry();
    case 7: return faker.beer().name();
    case 8: return faker.commerce().department();
    case 9: return faker.name().firstName();
    case 10: return faker.name().username();
    default: return faker.name().fullName();
    }
  }

  private int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max -min )) + min);
  }

  private Clienti generateRandomCliente() {
    Faker faker = new Faker(new Locale("it"));
    Clienti cliente = new Clienti();

    cliente.ragioneSociale = faker.company().name();
    cliente.cap = faker.address().zipCode();
    cliente.citta = faker.address().city();
    cliente.indirizzo = faker.address().streetAddress();
    cliente.partitaIva = faker.numerify("###########");
    cliente.regione = faker.address().state();
    cliente.telefono = faker.phoneNumber().phoneNumber();

    return cliente;

  }

  private Dipendenti generateRandomDipendente(long idAzienda) {
    Faker faker = new Faker(new Locale("it"));
    Dipendenti dipendente = new Dipendenti();

    dipendente.idAzienda = idAzienda;
    dipendente.nome = faker.name().firstName();
    dipendente.cognome = faker.name().lastName();
    dipendente.citta = faker.address().city();
    dipendente.regione = faker.address().state();
    dipendente.indirizzo = faker.address().streetAddress();
    dipendente.email = faker.internet().emailAddress().replace(" ","");
    dipendente.telefono = faker.phoneNumber().cellPhone();
    dipendente.cap = Integer.parseInt(faker.address().zipCode());

    return dipendente;
  }
}
