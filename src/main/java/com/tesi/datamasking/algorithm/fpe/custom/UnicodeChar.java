package com.tesi.datamasking.algorithm.fpe.custom;

import com.idealista.fpe.config.Alphabet;

public class UnicodeChar implements Alphabet {
  private static char[] CHARS = new char[91];

  public UnicodeChar() {
    initialize();
  }

  public char[] availableCharacters() {
    return CHARS;
  }

  public Integer radix() {
    return CHARS.length;
  }

  private void initialize() {
    for (int i = 32; i <= 122; i++) {
      CHARS[i - 32] = (char) i;
    }
  }
}