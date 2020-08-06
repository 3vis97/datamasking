package com.tesi.datamasking.algorithm.fpe;

import com.idealista.fpe.FormatPreservingEncryption;
import com.idealista.fpe.builder.FormatPreservingEncryptionBuilder;
import com.idealista.fpe.config.GenericDomain;
import com.idealista.fpe.config.GenericTransformations;
import com.tesi.datamasking.algorithm.fpe.custom.AlphaNumericChar;
import com.tesi.datamasking.algorithm.fpe.custom.CustomAlphabet;
import com.tesi.datamasking.algorithm.fpe.custom.EmailChars;
import com.tesi.datamasking.algorithm.fpe.custom.EnumChars;
import com.tesi.datamasking.algorithm.fpe.custom.UnicodeChars;
import com.tesi.datamasking.context.DataCrypt;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

public class FPE_Impl {

  private final String KEY_1;
  private final String KEY_2;
  private EnumChars enumChars;

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
    enumChars = EnumChars.DEFAULT;
  }

  public void useCustomAlphabet() {
    CustomAlphabet customAlphabet = new CustomAlphabet();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(customAlphabet, new GenericTransformations(customAlphabet.availableCharacters()), new GenericTransformations(customAlphabet.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChars = EnumChars.CUSTOM;
  }

  public void useAlphaNumericChars() {
    AlphaNumericChar alphaNumericChar = new AlphaNumericChar();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(alphaNumericChar, new GenericTransformations(alphaNumericChar.availableCharacters()), new GenericTransformations(alphaNumericChar.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChars = EnumChars.CUSTOM;
  }

  public void useEmailChars() {
    EmailChars emailChars = new EmailChars();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(
            emailChars, new GenericTransformations(emailChars.availableCharacters()), new GenericTransformations(
            emailChars.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
    enumChars = EnumChars.EMAIL;
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
    enumChars = EnumChars.UNICODE;
  }

  public String encryptString(String stringToEncrypt) {
      return formatPreservingEncryption.encrypt(stringToEncrypt, KEY_2.getBytes());
  }

  public String decryptString(String stringToDecrypt) {
    return formatPreservingEncryption.decrypt(stringToDecrypt, KEY_2.getBytes());
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
              if (!enumChars.equals(EnumChars.CUSTOM))
                useCustomAlphabet();
              field.set(classToCrypt, encryptString(field.get(classToCrypt).toString()));
            }
          }
          else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.EMAIL)) {
            if (field.getType().equals(String.class)) {
              if (!enumChars.equals(EnumChars.EMAIL))
                useEmailChars();
              field.set(classToCrypt, encryptEmail(field.get(classToCrypt).toString()));
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
              if (!enumChars.equals(EnumChars.CUSTOM))
                useCustomAlphabet();
              field.set(classToCrypt, decryptString(field.get(classToCrypt).toString()));
            }
          }
          else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.EMAIL)) {
            if (field.getType().equals(String.class)) {
              if (!enumChars.equals(EnumChars.EMAIL))
                useEmailChars();
              field.set(classToCrypt, decryptEmail(field.get(classToCrypt).toString()));
            }
          }
        }
      }
    }
  }


}
