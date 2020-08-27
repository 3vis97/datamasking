package com.tesi.datamasking.core;

import com.tesi.datamasking.algorithm.CryptDecrypt;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;

public class CoreFacade {

  protected void decryptSingleEntity(PseudonymizationSetup setup,
      Object entity,
      JpaRepository repository,
      CryptDecrypt cryptDecrypt)
      throws Exception {
    cryptDecrypt.decryptClass(entity, Arrays.asList(setup.fields));
    repository.save(entity);
  }

  protected void cryptSingleEntity(PseudonymizationSetup setup,
      Object entity,
      JpaRepository repository,
      CryptDecrypt cryptDecrypt)
      throws Exception {
    cryptDecrypt.cryptClass(entity, Arrays.asList(setup.fields));
    repository.save(entity);
  }

  protected Object cryptSingleEntityAndReturn(PseudonymizationSetup setup,
      Object entity,
      JpaRepository repository,
      CryptDecrypt cryptDecrypt)
      throws Exception {
    cryptDecrypt.cryptClass(entity, Arrays.asList(setup.fields));
    return entity;
  }

  protected Object decryptSingleEntityAndReturn(PseudonymizationSetup setup,
      Object entity,
      JpaRepository repository,
      CryptDecrypt cryptDecrypt)
      throws Exception {
    cryptDecrypt.decryptClass(entity, Arrays.asList(setup.fields));
    return entity;
  }

}
