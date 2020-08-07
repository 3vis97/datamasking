package com.tesi.datamasking.algorithm.fpe.custom;

import com.idealista.fpe.config.Alphabet;

public class UnicodeChar implements Alphabet {
  private static char[] CHARS = new char[65536];

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
    //iterate through 65535 chars
    for (int i = 0; i <= CHARS.length-1; i++) {
      CHARS[i] = (char) i;
    }
  }
}