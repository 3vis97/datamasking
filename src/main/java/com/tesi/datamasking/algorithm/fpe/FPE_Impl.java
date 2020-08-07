package com.tesi.datamasking.algorithm.fpe;

import com.idealista.fpe.FormatPreservingEncryption;
import com.idealista.fpe.builder.FormatPreservingEncryptionBuilder;
import com.idealista.fpe.config.GenericDomain;
import com.idealista.fpe.config.GenericTransformations;
import com.tesi.datamasking.algorithm.fpe.custom.AlphaNumericChar;
import com.tesi.datamasking.algorithm.fpe.custom.CustomChar;
import com.tesi.datamasking.algorithm.fpe.custom.EmailChar;
import com.tesi.datamasking.algorithm.fpe.custom.EnumChar;
import com.tesi.datamasking.algorithm.fpe.custom.NumericChar;
import com.tesi.datamasking.algorithm.fpe.custom.UnicodeChar;
import com.tesi.datamasking.context.DataCrypt;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

public class FPE_Impl {

  private final String KEY_1;
  private final String KEY_2;
  private EnumChar enumChar;

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
    enumChar = EnumChar.DEFAULT;
  }

  public void useCustomCharset() {
    CustomChar customChar = new CustomChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(customChar, new GenericTransformations(customChar.availableCharacters()), new GenericTransformations(
            customChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.CUSTOM;
  }

  public void useAlphaNumericCharset() {
    AlphaNumericChar alphaNumericChar = new AlphaNumericChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(alphaNumericChar, new GenericTransformations(alphaNumericChar.availableCharacters()), new GenericTransformations(alphaNumericChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.ALPHANUMERIC;
  }

  public void useEmailCharset() {
    EmailChar emailChar = new EmailChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(
            emailChar, new GenericTransformations(emailChar.availableCharacters()), new GenericTransformations(
            emailChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.EMAIL;
  }

  public void useUnicodeCharset() {
    UnicodeChar unicodeChar = new UnicodeChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(
            unicodeChar, new GenericTransformations(unicodeChar.availableCharacters()), new GenericTransformations(
            unicodeChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.UNICODE;
  }

  public void useNumericCharset() {
    NumericChar numericChar = new NumericChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(
            numericChar, new GenericTransformations(numericChar.availableCharacters()), new GenericTransformations(
            numericChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChar = EnumChar.NUMBER;
  }

  public String encryptString(String stringToEncrypt) {
      return formatPreservingEncryption.encrypt(stringToEncrypt, KEY_2.getBytes());
  }

  public String decryptString(String stringToDecrypt) {
    return formatPreservingEncryption.decrypt(stringToDecrypt, KEY_2.getBytes());
  }

  public Integer encryptInt(Integer integer){
    return Integer.valueOf(formatPreservingEncryption.encrypt(integer.toString(), KEY_2.getBytes()));
  }

  public Integer decryptInt(Integer integer){
    return Integer.valueOf(formatPreservingEncryption.decrypt(integer.toString(), KEY_2.getBytes()));
  }

  public String encryptEmail(String emailToEncrypt) {
    String localPart = emailToEncrypt.split("@")[0];
    String domainPart = emailToEncrypt.split("@")[1];

    return encryptPart(localPart) + "@" + encryptPart(domainPart);
  }

  private String encryptPart(String totalPart) {
    String result = "";
    String[] subPart = totalPart.split("\\.");
    for (String part : subPart) {
      result += encryptString(part) + ".";
    }
    return StringUtils.chop(result);
  }

  public String decryptEmail(String emailToDecrypt) {
    String localPart = emailToDecrypt.split("@")[0];
    String domainPart = emailToDecrypt.split("@")[1];

    return decryptPart(localPart) + "@" + decryptPart(domainPart);
  }

  private String decryptPart(String totalPart) {
    String result = "";
    String[] subPart = totalPart.split("\\.");
    for (String part : subPart) {
      result += decryptString(part) + ".";
    }
    return StringUtils.chop(result);
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
              if (!enumChar.equals(EnumChar.CUSTOM))
                useCustomCharset();
              field.set(classToCrypt, encryptString(field.get(classToCrypt).toString()));
            }
          }
          else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.EMAIL)) {
            if (field.getType().equals(String.class)) {
              if (!enumChar.equals(EnumChar.EMAIL))
                useEmailCharset();
              field.set(classToCrypt, encryptEmail(field.get(classToCrypt).toString()));
            }
          }
          else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.NUMBER)) {
            if (field.getType().equals(Integer.class)) {
              if (!enumChar.equals(EnumChar.NUMBER))
                useNumericCharset();
            }
            field.set(classToCrypt, decryptInt((Integer) field.get(classToCrypt)));
          }
        }
      }
    }
  }

  public void decryptClass(Object classToDecrypt, List<String> fieldsToDecrypt) throws IllegalAccessException {
    Field[] classFields = classToDecrypt.getClass().getFields();
    for (Field field : classFields) {
      if (fieldsToDecrypt.contains(field.getName())) {
        field.setAccessible(true);
        if (field.isAnnotationPresent(DataCrypt.class)) {
          DataCrypt dataCryptInstance = field.getAnnotation(DataCrypt.class);
          if (dataCryptInstance.dataType().equals(DataCrypt.DataType.DEFAULT_UNICODE)) {
            if (field.getType().equals(String.class)) {
              if (!enumChar.equals(EnumChar.CUSTOM))
                useCustomCharset();
              field.set(classToDecrypt, decryptString(field.get(classToDecrypt).toString()));
            }
          }
          else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.EMAIL)) {
            if (field.getType().equals(String.class)) {
              if (!enumChar.equals(EnumChar.EMAIL))
                useEmailCharset();
              field.set(classToDecrypt, decryptEmail(field.get(classToDecrypt).toString()));
            }
          }
          else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.NUMBER)) {
            if (field.getType().equals(Integer.class)) {
              if (!enumChar.equals(EnumChar.NUMBER))
                useNumericCharset();
            }
            field.set(classToDecrypt, decryptInt((Integer) field.get(classToDecrypt)));
          }
        }
      }
    }
  }


}
