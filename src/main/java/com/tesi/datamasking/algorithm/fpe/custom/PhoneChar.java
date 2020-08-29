package com.tesi.datamasking.algorithm.fpe.custom;

import com.idealista.fpe.config.Alphabet;

public class PhoneChar implements Alphabet {
  private static final char[] CHARS = new char[]
      { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', '-', '(', ')', ' ' };

  public PhoneChar() {
  }

  public char[] availableCharacters() {
    return CHARS;
  }

  public Integer radix() {
    return CHARS.length;
  }
}