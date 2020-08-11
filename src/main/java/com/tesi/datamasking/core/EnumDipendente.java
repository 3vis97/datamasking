package com.tesi.datamasking.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public enum EnumDipendente {
  DIRIGENTE(3000.00, 4), IMPIEGATO(1500.00, 3), STAGISTA(600.00, 2), PART_TIME(900.00, 1);

  EnumDipendente(double amount, int multiplier) {
    this.baseAmount = amount;
    this.multiplier = multiplier;
  }

  private double baseAmount;
  private int multiplier;

  private static final List<EnumDipendente> VALUES =
      Collections.unmodifiableList(Arrays.asList(values()));
  private static final int SIZE = VALUES.size();
  private static final Random RANDOM = new Random();

  public static EnumDipendente randomDipendente()  {
    return VALUES.get(RANDOM.nextInt(SIZE));
  }

  public double getBaseAmount() {
    double upper = multiplier * 50.00;
    double lower = - multiplier * 50.00;
    return baseAmount * (upper - lower) + lower;
  }

}
