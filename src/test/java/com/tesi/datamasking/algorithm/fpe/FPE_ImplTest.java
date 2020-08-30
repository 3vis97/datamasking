package com.tesi.datamasking.algorithm.fpe;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.equalTo;

class FPE_ImplTest {

  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  @Nested
  class test {

    @Test
    public void check_decryption() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3214qaa";

      String secret = "abcaefoza";

      FPE fpe = new FPE(key1, key2);
      String secretEncrypted = fpe.encryptString(secret);
      String secretDecrypted = fpe.decryptString(secretEncrypted);

      Assert.assertThat(secretDecrypted, equalTo(secret));
    }

    @Test
    public void check_decryption_with_spaces() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3ad21414";

      String secret = "LAST day of JUNE";

      FPE fpe = new FPE(key1, key2);
      fpe.useCustomCharset();
      String secretEncrypted = fpe.encryptString(secret);
      String secretDecrypted = fpe.decryptString(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(secret));
    }

    @Test
    public void check_decryption_with_samechars() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3ad21414";

      String secret = "aaaaaa";

      FPE fpe = new FPE(key1, key2);
      fpe.useCustomCharset();
      String secretEncrypted = fpe.encryptString(secret);
      String secretDecrypted = fpe.decryptString(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(secret));
    }

    @Test
    public void check_length() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3ad21414";

      String secret = "abcdefghijklmopqrstuvzABCV";

      FPE fpe = new FPE(key1, key2);
      fpe.useCustomCharset();
      String secretEncrypted = fpe.encryptString(secret);

      Assert.assertThat(secretEncrypted.length(), CoreMatchers.is(secret.length()));
    }

    @Test
    public void check_decryption_with_alphanum() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3ad21414";

      String secret = "a13caBAZ";

      FPE fpe = new FPE(key1, key2);
      fpe.useUnicodeCharset();
      String secretEncrypted = fpe.encryptString(secret);
      String secretDecrypted = fpe.decryptString(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(secret));
    }

    @Test
    public void check_decryption_with_email() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3ad21414";

      String secret = "massimo.longobardo@gmail.com";

      FPE fpe = new FPE(key1, key2);
      fpe.useEmailCharset();
      String secretEncrypted = fpe.encryptEmail(secret);
      String secretDecrypted = fpe.decryptEmail(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(secret));
    }

    @Test
    public void check_decryption_with_number() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3ad21414";

      String secret = "123456789";

      FPE fpe = new FPE(key1, key2);
      fpe.useNumericCharset();
      String secretEncrypted = fpe.encryptString(secret);
      String secretDecrypted = fpe.decryptString(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(secret));
    }

    @Test
    public void check_decryption_with_decimal() {
      String key1 = "ssdkF$HUy2A#D%kd";
      String key2 = "weJiSEvR5yAC5ftB";

      String secret = "852.91";

      FPE fpe = new FPE(key1, key2);
      fpe.useNumericCharset();
      BigDecimal secretEncrypted = fpe.encryptAmount(new BigDecimal(secret));
      BigDecimal secretDecrypted = fpe.decryptAmount(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(new BigDecimal(secret)));
    }

    public void printAllAlphaChars() {
      char[] CHARS = new char[91];
      //iterate through 65535 chars
      for (int i = 32; i <= 122; i++) {
        CHARS[i - 32] = (char) i;
        System.out.println("CHAR " + (i - 32) + ":" + CHARS[i - 32]);
      }
    }
  }

}