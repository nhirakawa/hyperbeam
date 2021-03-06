package com.github.nhirakawa.hyperbeam.shape;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public abstract class AxisAlignedBoundingBoxModel {

  public abstract Vector3 getMin();

  public abstract Vector3 getMax();

  public boolean hit(Ray ray, double tMin, double tMax) {
    for (int a = 0; a < 3; a++) {
      double t0 = ffMin(
        (getMin().get(a) - ray.getOrigin().get(a)) / ray.getDirection().get(a),
        (getMax().get(a) - ray.getOrigin().get(a)) / ray.getDirection().get(a)
      );
      double t1 = ffMax(
        (getMin().get(a) - ray.getOrigin().get(a)) / ray.getDirection().get(a),
        (getMin().get(a) - ray.getOrigin().get(a)) / ray.getDirection().get(a)
      );

      tMin = ffMax(t0, tMin);
      tMax = ffMax(t1, tMax);

      if (tMax <= tMin) {
        return false;
      }
    }

    return true;
  }

  public static AxisAlignedBoundingBox getSurroundingBox(
    AxisAlignedBoundingBox box0,
    AxisAlignedBoundingBox box1
  ) {
    Vector3 min = Vector3
      .builder()
      .setX(Double.min(box0.getMin().getX(), box1.getMin().getX()))
      .setY(Double.min(box0.getMin().getY(), box1.getMin().getY()))
      .setZ(Double.min(box0.getMin().getZ(), box1.getMin().getZ()))
      .build();

    Vector3 max = Vector3
      .builder()
      .setX(Double.min(box0.getMax().getX(), box1.getMax().getX()))
      .setY(Double.min(box0.getMax().getY(), box1.getMax().getY()))
      .setZ(Double.min(box0.getMax().getZ(), box1.getMax().getZ()))
      .build();

    return AxisAlignedBoundingBox.builder().setMin(min).setMax(max).build();
  }

  private static double ffMin(double a, double b) {
    if (a < b) {
      return a;
    }

    return b;
  }

  private static double ffMax(double a, double b) {
    if (a > b) {
      return a;
    }

    return b;
  }
}
