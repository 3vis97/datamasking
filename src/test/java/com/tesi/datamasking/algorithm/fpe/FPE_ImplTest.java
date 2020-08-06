package com.tesi.datamasking.algorithm.fpe;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FPE_ImplTest {

  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  @Nested
  class test {

    @Test
    public void check_decryption() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3214qaa";

      String secret = "abcaefoza";

      FPE_Impl fpe = new FPE_Impl(key1, key2);
      String secretEncrypted = fpe.encryptString(secret);
      String secretDecrypted = fpe.decryptString(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(secret));
    }

    @Test
    public void check_decryption_with_spaces() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3ad21414";

      String secret = "LAST day of JUNE";

      FPE_Impl fpe = new FPE_Impl(key1, key2);
      fpe.useCustomAlphabet();
      String secretEncrypted = fpe.encryptString(secret);
      String secretDecrypted = fpe.decryptString(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(secret));
    }

    @Test
    public void check_decryption_with_samechars() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3ad21414";

      String secret = "aaaaaa";

      FPE_Impl fpe = new FPE_Impl(key1, key2);
      fpe.useCustomAlphabet();
      String secretEncrypted = fpe.encryptString(secret);
      String secretDecrypted = fpe.decryptString(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(secret));
    }

    @Test
    public void check_decryption_with_alphanum() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3ad21414";

      String secret = "a13caBAZè{#@";

      FPE_Impl fpe = new FPE_Impl(key1, key2);
      fpe.useUnicodeChar();
      String secretEncrypted = fpe.encryptString(secret);
      String secretDecrypted = fpe.decryptString(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(secret));
    }

  }

}