package com.tesi.datamasking.algorithm.fpe.custom;

import com.idealista.fpe.config.Alphabet;

public class CustomChar implements Alphabet {
  private static final char[] CHARS = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',' '};

  public CustomChar() {
  }

  public char[] availableCharacters() {
    return CHARS;
  }

  public Integer radix() {
    return CHARS.length;
  }
}