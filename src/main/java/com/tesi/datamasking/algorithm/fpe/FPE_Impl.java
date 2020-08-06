package com.tesi.datamasking.algorithm.fpe;

import com.idealista.fpe.FormatPreservingEncryption;
import com.idealista.fpe.builder.FormatPreservingEncryptionBuilder;
import com.idealista.fpe.config.GenericDomain;
import com.idealista.fpe.config.GenericTransformations;
import com.tesi.datamasking.algorithm.fpe.custom.AllChars;
import com.tesi.datamasking.algorithm.fpe.custom.CustomAlphabet;
import com.tesi.datamasking.algorithm.fpe.custom.UnicodeChars;
import com.tesi.datamasking.context.DataCrypt;

import java.lang.reflect.Field;
import java.util.List;

public class FPE_Impl {

  private final String KEY_1;
  private final String KEY_2;

  private FormatPreservingEncryption formatPreservingEncryption;

  public FPE_Impl(String KEY_1, String KEY_2){
    this.KEY_1 = KEY_1;
    this.KEY_2 = KEY_2;
    useDefault();
  }

  public void useDefault() {
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDefaultDomain()
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
  }

  public void useCustomAlphabet() {
    CustomAlphabet customAlphabet = new CustomAlphabet();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(customAlphabet, new GenericTransformations(customAlphabet.availableCharacters()), new GenericTransformations(customAlphabet.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
  }

  public void useAlphaNumeric() {
    AllChars allChars = new AllChars();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(
            allChars, new GenericTransformations(allChars.availableCharacters()), new GenericTransformations(allChars.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
  }

  public void useUnicodeChar() {
    UnicodeChars unicodeChars = new UnicodeChars();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(
            unicodeChars, new GenericTransformations(unicodeChars.availableCharacters()), new GenericTransformations(unicodeChars.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
  }

  public String encryptString(String stringToEncrypt) {
      return formatPreservingEncryption.encrypt(stringToEncrypt, KEY_2.getBytes());
  }

  public String decryptString(String stringToDecrypt) {
    return formatPreservingEncryption.decrypt(stringToDecrypt, KEY_2.getBytes());
  }


  public void cryptClass(Object classToCrypt, List<String> fieldsToCrypt) throws IllegalAccessException {
    Field[] classFields = classToCrypt.getClass().getFields();
    for (Field field : classFields) {
      if (fieldsToCrypt.contains(field.getName())) {
        field.setAccessible(true);
        if (field.isAnnotationPresent(DataCrypt.class)) {
          DataCrypt dataCryptInstance = field.getAnnotation(DataCrypt.class);
          if (dataCryptInstance.dataType().equals(DataCrypt.DataType.DEFAULT_UNICODE)) {
            if (field.getType().equals(String.class)) {
              field.set(classToCrypt, encryptString(field.get(classToCrypt).toString()));
            }
          }
        }
      }
    }
  }

  public void decryptClass(Object classToCrypt, List<String> fieldsToCrypt) throws IllegalAccessException {
    Field[] classFields = classToCrypt.getClass().getFields();
    for (Field field : classFields) {
      if (fieldsToCrypt.contains(field.getName())) {
        field.setAccessible(true);
        if (field.isAnnotationPresent(DataCrypt.class)) {
          DataCrypt dataCryptInstance = field.getAnnotation(DataCrypt.class);
          if (dataCryptInstance.dataType().equals(DataCrypt.DataType.DEFAULT_UNICODE)) {
            if (field.getType().equals(String.class)) {
              field.set(classToCrypt, decryptString(field.get(classToCrypt).toString()));
            }
          }
        }
      }
    }
  }


}
