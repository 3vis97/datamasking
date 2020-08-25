package com.tesi.datamasking.core;

import com.github.javafaker.Faker;
import com.tesi.datamasking.algorithm.CryptDecrypt;
import com.tesi.datamasking.data.db.amounts.Amounts;
import com.tesi.datamasking.data.db.amounts.AmountsRepository;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

public class TestFacade extends CoreFacade {

  private final AmountsRepository amountsRepository;
  private final Faker faker;
  private final CryptDecrypt cryptDecrypt;

  private static final Logger LOGGER = LoggerFactory.getLogger(TestFacade.class);

  private int amountCodeId = 1;

  @Autowired
  public TestFacade(AmountsRepository amountsRepository,
      Faker faker,
      CryptDecrypt cryptDecrypt) {
    this.amountsRepository = amountsRepository;
    this.cryptDecrypt = cryptDecrypt;
    this.faker = faker;
  }

  public void populateRandomAmounts(long amounts) {
    List<Amounts> amountsList = new ArrayList<>();

    for (int i = 0; i < amounts; i++) {
      EnumEmployeeJob enumEmployeeJob = EnumEmployeeJob.getRandomEmployeeJob();
      Amounts amountsSaved = generateRandomAmounts(amountCodeId++, enumEmployeeJob);
      amountsList.add(amountsSaved);
    }
    amountsRepository.insertWithBatchInsert(amountsList);
  }

  private List<Amounts> getAllAmounts() {
    return amountsRepository.findAll();
  }

  private Amounts generateRandomAmounts(int code,
      EnumEmployeeJob enumEmployeeJob) {
    Amounts amounts = new Amounts();
    amounts.code = generateNextAmountCode(code);
    amounts.price = BigDecimal.valueOf(enumEmployeeJob.getBaseAmount());
    return amounts;
  }

  private String generateNextAmountCode(int code) {
    return "A" + String.format("%07d", code);
  }

  public void decryptAmount(String code,
      PseudonymizationSetup setup)
      throws InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
      IllegalAccessException {
    Amounts amountsToDecrypt = amountsRepository.findById(code).get();
    decryptSingleEntity(setup, amountsToDecrypt, amountsRepository, cryptDecrypt);
  }

  public void cryptAmount(String code,
      PseudonymizationSetup setup)
      throws InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
      IllegalAccessException {
    Amounts amountsToCrypt = amountsRepository.findById(code).get();
    cryptSingleEntity(setup, amountsToCrypt, amountsRepository, cryptDecrypt);
  }

  public void cryptAllAmounts(PseudonymizationSetup setup) throws Exception {
    List<Amounts> allAmounts = getAllAmounts();
    String currentCode = "";
    try {
      for (Amounts amount : allAmounts) {
        currentCode = amount.code;
        cryptSingleEntity(setup, amount, amountsRepository, cryptDecrypt);
      }
    } catch (Exception e) {
      throw new Exception("Error while processing code " + currentCode + ". Details :" + e.getMessage());
    }

  }

  public void decryptAllAmounts(PseudonymizationSetup setup)
      throws Exception {
    List<Amounts> allAmounts = getAllAmounts();
    String currentCode = "";
    try {
      for (Amounts amount : allAmounts) {
        currentCode = amount.code;
        decryptSingleEntity(setup, amount, amountsRepository, cryptDecrypt);
      }
    } catch (Exception e) {
      throw new Exception("Error while processing code " + currentCode + ". Details :" + e.getMessage());
    }
  }

}
