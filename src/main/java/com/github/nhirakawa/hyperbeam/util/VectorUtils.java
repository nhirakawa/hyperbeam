package com.github.nhirakawa.hyperbeam.util;

import static com.github.nhirakawa.hyperbeam.util.MathUtils.rand;

import java.math.BigDecimal;

import com.github.nhirakawa.hyperbeam.geometry.Vector3;

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

  public static Vector3 getRandomVectorInUnitDisk() {
    while (true) {
      Vector3 point = new Vector3(rand(), rand(), 0).scalarMultiply(2).subtract(new Vector3(1, 1, 0));
      if(point.dotProduct(point) < 1) {
        return point;
      }
    }
  }

}
