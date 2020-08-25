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
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Random;

public class FPE {

  private final String KEY_1;
  private final String KEY_2;
  private EnumChar enumChar;

  private FormatPreservingEncryption formatPreservingEncryption;

  public FPE(String KEY_1,
      String KEY_2) {
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

  public Integer decryptInt(Integer integer) {
    return Integer.valueOf(formatPreservingEncryption.decrypt(integer.toString(), KEY_2.getBytes()));
  }

  public String encryptEmail(String emailToEncrypt) {
    return encryptPart(emailToEncrypt.split("@")[0]) + "@" + encryptPart(emailToEncrypt.split("@")[1]);
  }

  public String decryptEmail(String emailToDecrypt) {
    return decryptPart(emailToDecrypt.split("@")[0]) + "@" + decryptPart(emailToDecrypt.split("@")[1]);
  }

  public BigDecimal encryptAmount(BigDecimal value) {
    String doubleAsString = String.valueOf(value);
    String leftSide = randomChar() + encryptString(doubleAsString.split("\\.")[0]);
    String rightSide = encryptString(doubleAsString.split("\\.")[1]);

    return new BigDecimal(leftSide + "." + rightSide);
  }

  public BigDecimal decryptAmount(BigDecimal value) {
    String doubleAsString = String.valueOf(value);
    return new BigDecimal(decryptString(doubleAsString.split("\\.")[0].substring(1)) + "." +
        decryptString(doubleAsString.split("\\.")[1]));
  }

  private char randomChar() {
    final String num = "123456789";
    final int N = num.length();

    Random random = new Random();
    return num.charAt(random.nextInt(N));
  }

  private String encryptPart(String totalPart) {
    StringBuilder result = new StringBuilder();
    String[] subPart = totalPart.split("\\.");
    for (String part : subPart) {
      result.append(encryptString(part)).append(".");
    }
    return StringUtils.chop(result.toString()); //remove last dot
  }

  private String decryptPart(String totalPart) {
    StringBuilder result = new StringBuilder();
    String[] subPart = totalPart.split("\\.");
    for (String part : subPart) {
      result.append(decryptString(part)).append(".");
    }
    return StringUtils.chop(result.toString());
  }

  public EnumChar getEnumChar() {
    return enumChar;
  }
}
