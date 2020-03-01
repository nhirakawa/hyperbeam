package com.github.nhirakawa.hyperbeam.util;

import static com.github.nhirakawa.hyperbeam.util.MathUtils.rand;

import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import java.math.BigDecimal;

public final class VectorUtils {
  private static final Vector3 DISK_VECTOR = Vector3
    .builder()
    .setX(1)
    .setY(1)
    .setZ(0)
    .build();

  private VectorUtils() {}

  public static Vector3 getRandomUnitSphereVector() {
    while (true) {
      Vector3 point = Vector3
        .builder()
        .setX(rand())
        .setY(rand())
        .setZ(rand())
        .build()
        .scalarMultiply(2)
        .subtract(Vector3.one());

      if (
        BigDecimal.valueOf(point.getSquaredLength()).compareTo(BigDecimal.ONE) <
          0
      ) {
        return point;
      }
    }
  }

  public static Vector3 getRandomVectorInUnitDisk() {
    while (true) {
      Vector3 point = Vector3
        .builder()
        .setX(rand())
        .setY(rand())
        .setZ(rand())
        .build()
        .scalarMultiply(2)
        .subtract(DISK_VECTOR);

      if (point.dotProduct(point) < 1) {
        return point;
      }
    }
  }
}
