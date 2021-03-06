package com.tesi.datamasking.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public enum EnumEmployeeJob {
  DIRIGENTE(3000.00, 4), IMPIEGATO(1500.00, 3), STAGISTA(600.00, 2), PART_TIME(900.00, 1);

  EnumEmployeeJob(double amount,
      int multiplier) {
    this.baseAmount = amount;
    this.multiplier = multiplier;
  }

  private double baseAmount;
  private int multiplier;

  private static final List<EnumEmployeeJob> VALUES =
      Collections.unmodifiableList(Arrays.asList(values()));
  private static final int SIZE = VALUES.size();
  private static final Random RANDOM = new Random();

  public static EnumEmployeeJob getRandomEmployeeJob() {
    return VALUES.get(RANDOM.nextInt(SIZE));
  }

  public double getBaseAmount() {
    double upper = multiplier * 50.00;
    double lower = -multiplier * 50.00;
    double random = new Random().nextDouble();
    return baseAmount + (random * (upper - lower)) + lower;
  }

}
