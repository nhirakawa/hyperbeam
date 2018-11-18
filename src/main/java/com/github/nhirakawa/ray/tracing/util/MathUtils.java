package com.github.nhirakawa.ray.tracing.util;

import java.util.concurrent.ThreadLocalRandom;

public final class MathUtils {

  private MathUtils() {}

  public static double rand() {
    return ThreadLocalRandom.current().nextDouble();
  }

}
