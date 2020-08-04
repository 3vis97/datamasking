package com.tesi.datamasking.algorithm.crypt;

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

class EncryptDecryptTest {

  @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
  @Nested
  class test {

    @Test
    public void check_decryption()
        throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException,
        IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
      String key1 = "12Cd#94qpz!%4/(0";
      String key2 = "353fwafwg3plofmv";

      String secret = "this is a phrase to be encrypt";

      EncryptDecrypt encryptDecrypt = new EncryptDecrypt(key1, key2);
      String secretEncrypted = encryptDecrypt.encrypt(secret);
      String secretDecrypted = encryptDecrypt.decrypt(secretEncrypted);

      Assert.assertThat(secretDecrypted, is(secret));

    }
  }

}