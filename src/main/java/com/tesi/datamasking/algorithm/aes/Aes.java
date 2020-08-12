package com.tesi.datamasking.algorithm.aes;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Aes {

  private IvParameterSpec ivParameterSpec;
  private SecretKeySpec secretKeySpec;
  private Cipher cipher;

  public Aes(String SECRET_KEY_1,
      String SECRET_KEY_2) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException {
    ivParameterSpec = new IvParameterSpec(SECRET_KEY_1.getBytes("UTF-8"));
    secretKeySpec = new SecretKeySpec(SECRET_KEY_2.getBytes("UTF-8"), "AES");
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
      InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
    byte[] encrypted = cipher.doFinal(toBeEncrypt.getBytes());
    return Base64.encodeBase64String(encrypted);
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
  public String decrypt(String encrypted) throws InvalidAlgorithmParameterException, InvalidKeyException,
      BadPaddingException, IllegalBlockSizeException {
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
    byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encrypted));
    return new String(decryptedBytes);
  }
}
