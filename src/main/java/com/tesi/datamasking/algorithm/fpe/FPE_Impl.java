package com.tesi.datamasking.algorithm.fpe;

import com.idealista.fpe.FormatPreservingEncryption;
import com.idealista.fpe.builder.FormatPreservingEncryptionBuilder;
import com.idealista.fpe.component.functions.prf.DefaultPseudoRandomFunction;
import com.idealista.fpe.config.Defaults;
import com.idealista.fpe.config.GenericDomain;
import com.idealista.fpe.config.GenericTransformations;
import com.idealista.fpe.config.LengthRange;
import com.idealista.fpe.config.basic.BasicAlphabet;
import com.idealista.fpe.transformer.IntToTextTransformer;
import com.idealista.fpe.transformer.TextToIntTransformer;
import com.tesi.datamasking.algorithm.fpe.customAlphabet.CustomAlphabet;
import com.tesi.datamasking.context.DataCrypt;

import java.lang.reflect.Field;
import java.util.Arrays;
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

  public void useCustom() {
    CustomAlphabet customAlphabet = new CustomAlphabet();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(customAlphabet, new GenericTransformations(customAlphabet.availableCharacters()), new GenericTransformations(customAlphabet.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
  }

  public String encrypt(String stringToEncrypt) {
      return formatPreservingEncryption.encrypt(stringToEncrypt, KEY_2.getBytes());
  }

  public String decrypt(String stringToDecrypt) {
    return formatPreservingEncryption.decrypt(stringToDecrypt, KEY_2.getBytes());
  }


  public void cryptClass(Object classToCrypt, List<String> fieldsToCrypt) throws IllegalAccessException {
    Field[] classFields = classToCrypt.getClass().getFields();
    for (Field field : classFields) {
      if (fieldsToCrypt.contains(field.getName())) {
        field.setAccessible(true);
        if (field.isAnnotationPresent(DataCrypt.class)) {
          DataCrypt dataCryptInstance = field.getAnnotation(DataCrypt.class);
          if ((dataCryptInstance.dataType().equals(DataCrypt.DataType.FIRST_NAME)) ||
              (dataCryptInstance.dataType().equals(DataCrypt.DataType.LAST_NAME)) ||
              (dataCryptInstance.dataType().equals(DataCrypt.DataType.DEFAULT_STRING))) {
            if (field.getType().equals(String.class)) {
              field.set(classToCrypt, encrypt(field.get(classToCrypt).toString()));
            }
          }
        }
      }
    }
  }


}
