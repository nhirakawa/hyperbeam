package com.github.nhirakawa.ray.tracing.shape;

import java.util.Optional;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.collision.AxisAlignedBoundingBox;
import com.github.nhirakawa.ray.tracing.collision.HitRecord;
import com.github.nhirakawa.ray.tracing.collision.Hittable;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.material.Material;
import com.google.common.collect.Range;

@Value.Immutable
@ImmutableStyle
public abstract class MovingSphereModel implements Hittable {

  public abstract Vector3 getCenter0();
  public abstract Vector3 getCenter1();
  public abstract double getTime0();
  public abstract double getTime1();
  public abstract double getRadius();
  public abstract Material getMaterial();

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    Vector3 center = getCenter(ray.getTime());
    Vector3 oc = ray.getOrigin().subtract(center);

    double a = ray.getDirection().dotProduct(ray.getDirection());
    double b = oc.dotProduct(ray.getDirection());
    double c = oc.dotProduct(oc) - (getRadius() * getRadius());

    double discriminant = (b * b) - (a * c);

    if (discriminant > 0) {
      double negativeTemp = (-b - Math.sqrt(discriminant)) / a; // TODO rename this awful variable
      Range<Double> parameterRange = Range.open(tMin, tMax);

      if (parameterRange.contains(negativeTemp)) {
        Vector3 point = ray.getPointAtParameter(negativeTemp);
        Vector3 normal = point.subtract(center).scalarDivide(getRadius());
        return Optional.of(
            HitRecord.builder()
                .setT(negativeTemp)
                .setPoint(point)
                .setNormal(normal)
                .setMaterial(getMaterial())
                .build()
        );
      }

      double positiveTemp = (-b + Math.sqrt(discriminant)) / a; // TODO rename this awful variable
      if (parameterRange.contains(positiveTemp)) {
        Vector3 point = ray.getPointAtParameter(positiveTemp);
        Vector3 normal = point.subtract(center).scalarDivide(getRadius());
        return Optional.of(
            HitRecord.builder()
                .setT(positiveTemp)
                .setPoint(point)
                .setNormal(normal)
                .setMaterial(getMaterial())
                .build()
        );
      }
    }

    return Optional.empty();
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    Vector3 radiusVector = new Vector3(getRadius(), getRadius(), getRadius());

    AxisAlignedBoundingBox box0 = AxisAlignedBoundingBox.builder()
        .setMin(getCenter(t0).subtract(radiusVector))
        .setMax(getCenter(t0).add(radiusVector))
        .build();
    AxisAlignedBoundingBox box1 = AxisAlignedBoundingBox.builder()
        .setMin(getCenter(t1).subtract(radiusVector))
        .setMax(getCenter(t1).add(radiusVector))
        .build();

    return Optional.of(AxisAlignedBoundingBox.getSurroundingBox(box0, box1));
  }

  private Vector3 getCenter(double time) {
    double timeMultiplier = (time - getTime0()) / (getTime1() - getTime0());
    return getCenter0().add(getCenter1().subtract(getCenter0()).scalarMultiply(timeMultiplier));
  }

}
