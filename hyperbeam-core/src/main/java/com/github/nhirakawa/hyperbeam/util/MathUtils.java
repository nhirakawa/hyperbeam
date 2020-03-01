package com.github.nhirakawa.hyperbeam.util;

import java.util.Random;

public final class MathUtils {
  private static final Random RANDOM = new Random(1);

  private MathUtils() {}

  public static double rand() {
    return RANDOM.nextDouble();
  }
}
