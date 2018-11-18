package com.github.nhirakawa.ray.tracing.util;

import static com.github.nhirakawa.ray.tracing.util.MathUtils.rand;

import java.math.BigDecimal;

import com.github.nhirakawa.ray.tracing.geometry.Vector3;

public final class VectorUtils {

  private VectorUtils() {}

  public static Vector3 getRandomUnitSphereVector() {
    while (true) {
      Vector3 point = new Vector3(rand(), rand(), rand()).scalarMultiply(2).subtract(new Vector3(1, 1, 1));
      if (BigDecimal.valueOf(point.getSquaredLength()).compareTo(BigDecimal.ONE) < 0) {
        return point;
      }
    }
  }
}
