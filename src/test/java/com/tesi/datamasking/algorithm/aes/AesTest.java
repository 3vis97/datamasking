package com.tesi.datamasking.algorithm.aes;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.CoreMatchers.is;

class AesTest {

  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  @Nested
  class test {

    @Test
    public void check_decryption()
        throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException,
        IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3plofmv";

      String secret = "this is a phrase to be encryptString";

      Aes aes = new Aes(key1, key2);
      String secretEncrypted = aes.encrypt(secret);
      String secretDecrypted = aes.decrypt(secretEncrypted);

      Assert.assertThat(secretDecrypted, is(secret));

    }
  }

}