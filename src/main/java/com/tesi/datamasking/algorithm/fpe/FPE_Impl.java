package com.tesi.datamasking.algorithm.fpe;

import com.idealista.fpe.FormatPreservingEncryption;
import com.idealista.fpe.builder.FormatPreservingEncryptionBuilder;
import com.idealista.fpe.component.functions.prf.DefaultPseudoRandomFunction;
import com.idealista.fpe.config.Defaults;
import com.idealista.fpe.config.GenericDomain;
import com.idealista.fpe.config.GenericTransformations;
import com.idealista.fpe.config.LengthRange;
import com.idealista.fpe.config.basic.BasicAlphabet;
import com.idealista.fpe.transformer.IntToTextTransformer;
import com.idealista.fpe.transformer.TextToIntTransformer;
import com.tesi.datamasking.algorithm.fpe.customAlphabet.CustomAlphabet;

public class FPE_Impl {

  private final String KEY_1;
  private final String KEY_2;

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
  }

  public void useCustom() {
    CustomAlphabet customAlphabet = new CustomAlphabet();
    formatPreservingEncryption = FormatPreservingEncryptionBuilder
        .ff1Implementation()
        .withDomain(new GenericDomain(customAlphabet, new GenericTransformations(customAlphabet.availableCharacters()), new GenericTransformations(customAlphabet.availableCharacters())))
        .withDefaultPseudoRandomFunction(KEY_1.getBytes())
        .withDefaultLengthRange()
        .build();
  }

  public String encrypt(String stringToEncrypt) {
      return formatPreservingEncryption.encrypt(stringToEncrypt, KEY_2.getBytes());
  }

  public String decrypt(String stringToDecrypt) {
    return formatPreservingEncryption.decrypt(stringToDecrypt, KEY_2.getBytes());
  }


}
