package com.tesi.datamasking.core;

import com.tesi.datamasking.algorithm.CryptDecrypt;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Arrays;

public class CoreFacade {

  protected void decryptSingleEntity(PseudonymizationSetup setup,
      Object entity,
      JpaRepository repository,
      CryptDecrypt cryptDecrypt)
      throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
      InvalidKeyException, IllegalAccessException {
    cryptDecrypt.decryptClass(entity, Arrays.asList(setup.fields));
    repository.save(entity);
  }

  protected void cryptSingleEntity(PseudonymizationSetup setup,
      Object entity,
      JpaRepository repository,
      CryptDecrypt cryptDecrypt)
      throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
      InvalidKeyException, IllegalAccessException {
    cryptDecrypt.cryptClass(entity, Arrays.asList(setup.fields));
    repository.save(entity);
  }

}
