package com.tesi.datamasking.algorithm.aes;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Aes {

  private IvParameterSpec ivParameterSpec;
  private SecretKeySpec secretKeySpec;
  private Cipher cipher;

  public Aes(String SECRET_KEY_1,
      String SECRET_KEY_2) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
    ivParameterSpec = new IvParameterSpec(SECRET_KEY_1.getBytes(StandardCharsets.UTF_8));
    secretKeySpec = new SecretKeySpec(SECRET_KEY_2.getBytes(StandardCharsets.UTF_8), "AES");
    cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
  }


  /**
   * Encrypt the string with this internal algorithm.
   *
   * @param toBeEncrypt string object to be encryptString.
   * @return returns encrypted string.
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   */
  public String encrypt(String toBeEncrypt) throws
      Exception {
    try {
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] encryptedValue = cipher.doFinal(toBeEncrypt.getBytes(StandardCharsets.UTF_8));
      byte[] encodedValue = Base64.getEncoder().encode(encryptedValue);
      return new String(encodedValue, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new Exception("ENCRYPT EXCEPTION while encrypting " + toBeEncrypt + " - Details: " + e.toString());
    }
  }

  /**
   * Decrypt this string with the internal algorithm. The passed argument should be encrypted using
   * {@link #encrypt(String) encryptString} method of this class.
   *
   * @param encrypted encrypted string that was encrypted using {@link #encrypt(String) encryptString} method.
   * @return decrypted string.
   * @throws InvalidAlgorithmParameterException
   * @throws InvalidKeyException
   * @throws BadPaddingException
   * @throws IllegalBlockSizeException
   */
  public String decrypt(String encrypted) throws Exception {
    try {
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
      byte[] decodedValue = Base64.getDecoder().decode(encrypted.getBytes(StandardCharsets.UTF_8));
      byte[] decryptedValue = cipher.doFinal(decodedValue);
      return new String(decryptedValue, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new Exception("DECRYPT EXCEPTION while decrypt " + encrypted + " - Details: " + e.toString());
    }

  }
}
