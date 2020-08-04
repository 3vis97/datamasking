package com.tesi.datamasking.algorithm.fpe;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

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
      String secretEncrypted = fpe.encrypt(secret);
      String secretDecrypted = fpe.decrypt(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(secret));
    }

    @Test
    public void check_decryption_with_spaces() {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3ad21414";

      String secret = "abc aef oza";

      FPE_Impl fpe = new FPE_Impl(key1, key2);
      fpe.useCustom();
      String secretEncrypted = fpe.encrypt(secret);
      String secretDecrypted = fpe.decrypt(secretEncrypted);

      Assert.assertThat(secretDecrypted, CoreMatchers.is(secret));
    }

  }

}