package com.tesi.datamasking.algorithm;

import com.tesi.datamasking.algorithm.aes.Aes;
import com.tesi.datamasking.algorithm.fpe.FPE;
import com.tesi.datamasking.algorithm.fpe.custom.EnumChar;
import com.tesi.datamasking.context.DataCrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class CryptDecrypt {

  private FPE fpe;
  private Aes aes;

  public CryptDecrypt(String KEY_1,
      String KEY_2)
      throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException {
    this.fpe = new FPE(KEY_1, KEY_2);
    this.aes = new Aes(KEY_1, KEY_2);
  }

  public void cryptClass(Object classToCrypt,
      List<String> fieldsToCrypt)
      throws IllegalAccessException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
      InvalidKeyException {
    Field[] classFields = classToCrypt.getClass().getFields();
    for (Field field : classFields) {
      if (fieldsToCrypt.contains(field.getName())) {
        field.setAccessible(true);
        if (field.isAnnotationPresent(DataCrypt.class)) {
          DataCrypt dataCryptInstance = field.getAnnotation(DataCrypt.class);
          if (dataCryptInstance.dataType().equals(DataCrypt.DataType.DEFAULT_UNICODE)) {
            if (field.getType().equals(String.class)) {
              if (!fpe.getEnumChar().equals(EnumChar.CUSTOM))
                fpe.useCustomCharset();
              field.set(classToCrypt, fpe.encryptString(field.get(classToCrypt).toString()));
            }
          } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.EMAIL)) {
            if (field.getType().equals(String.class)) {
              if (!fpe.getEnumChar().equals(EnumChar.EMAIL))
                fpe.useEmailCharset();
              field.set(classToCrypt, fpe.encryptEmail(field.get(classToCrypt).toString()));
            }
          } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.NUMBER)) {
            if (field.getType().equals(Integer.class)) {
              if (!fpe.getEnumChar().equals(EnumChar.NUMBER))
                fpe.useNumericCharset();
              field.set(classToCrypt, fpe.encryptInt((Integer) field.get(classToCrypt)));
            }
          } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.LONG_STRING)) {
            if (field.getType().equals(String.class)) {
              field.set(classToCrypt, aes.encrypt(field.get(classToCrypt).toString()));
            }
          } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.AMOUNT)) {
            if (field.getType().equals(BigDecimal.class)) {
              field.set(classToCrypt, fpe.encryptAmount((BigDecimal) field.get(classToCrypt)));
            }
          }
        }
      }
    }
  }

  public void decryptClass(Object classToDecrypt,
      List<String> fieldsToDecrypt)
      throws IllegalAccessException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
      InvalidKeyException {
    Field[] classFields = classToDecrypt.getClass().getFields();
    for (Field field : classFields) {
      if (fieldsToDecrypt.contains(field.getName())) {
        field.setAccessible(true);
        if (field.isAnnotationPresent(DataCrypt.class)) {
          DataCrypt dataCryptInstance = field.getAnnotation(DataCrypt.class);
          if (dataCryptInstance.dataType().equals(DataCrypt.DataType.DEFAULT_UNICODE)) {
            if (field.getType().equals(String.class)) {
              if (!fpe.getEnumChar().equals(EnumChar.CUSTOM))
                fpe.useCustomCharset();
              field.set(classToDecrypt, fpe.decryptString(field.get(classToDecrypt).toString()));
            }
          } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.EMAIL)) {
            if (field.getType().equals(String.class)) {
              if (!fpe.getEnumChar().equals(EnumChar.EMAIL))
                fpe.useEmailCharset();
              field.set(classToDecrypt, fpe.decryptEmail(field.get(classToDecrypt).toString()));
            }
          } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.NUMBER)) {
            if (field.getType().equals(Integer.class)) {
              if (!fpe.getEnumChar().equals(EnumChar.NUMBER))
                fpe.useNumericCharset();
              field.set(classToDecrypt, fpe.decryptInt((Integer) field.get(classToDecrypt)));
            }
          } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.LONG_STRING)) {
            if (field.getType().equals(String.class)) {
              field.set(classToDecrypt, aes.decrypt(field.get(classToDecrypt).toString()));
            }
          } else if (dataCryptInstance.dataType().equals(DataCrypt.DataType.AMOUNT)) {
            if (field.getType().equals(BigDecimal.class)) {
              field.set(classToDecrypt, fpe.decryptAmount((BigDecimal) field.get(classToDecrypt)));
            }
          }
        }
      }
    }
  }

}
