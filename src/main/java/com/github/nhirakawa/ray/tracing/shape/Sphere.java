package com.github.nhirakawa.ray.tracing.shape;

import java.util.Optional;

import com.github.nhirakawa.ray.tracing.collision.AxisAlignedBoundingBox;
import com.github.nhirakawa.ray.tracing.collision.HitRecord;
import com.github.nhirakawa.ray.tracing.collision.Hittable;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.material.Material;
import com.google.common.collect.Range;

public class Sphere implements Hittable {

  private final Vector3 center;
  private final double radius;
  private final Material material;
  private final AxisAlignedBoundingBox axisAlignedBoundingBox;

  public Sphere(Vector3 center, double radius, Material material) {
    this.center = center;
    this.radius = radius;
    this.material = material;

    Vector3 radiusVector = new Vector3(radius, radius, radius);
    this.axisAlignedBoundingBox = AxisAlignedBoundingBox.builder()
        .setMin(center.subtract(radiusVector))
        .setMax(center.add(radiusVector))
        .build();
  }

  public Vector3 getCenter() {
    return center;
  }

  public double getRadius() {
    return radius;
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    Vector3 oc = ray.getOrigin().subtract(center);

    double a = ray.getDirection().dotProduct(ray.getDirection());
    double b = oc.dotProduct(ray.getDirection());
    double c = oc.dotProduct(oc) - (radius * radius);

    double discriminant = (b * b) - (a * c);

    if (discriminant > 0) {
      double negativeTemp = (-b - Math.sqrt(discriminant)) / a; // TODO rename this awful variable
      Range<Double> parameterRange = Range.open(tMin, tMax);

      if (parameterRange.contains(negativeTemp)) {
        Vector3 point = ray.getPointAtParameter(negativeTemp);
        Vector3 normal = point.subtract(center).scalarDivide(radius);
        return Optional.of(
            HitRecord.builder()
                .setT(negativeTemp)
                .setPoint(point)
                .setNormal(normal)
                .setMaterial(material)
                .build()
        );
      }

      double positiveTemp = (-b + Math.sqrt(discriminant)) / a; // TODO rename this awful variable
      if (parameterRange.contains(positiveTemp)) {
        Vector3 point = ray.getPointAtParameter(positiveTemp);
        Vector3 normal = point.subtract(center).scalarDivide(radius);
        return Optional.of(
            HitRecord.builder()
                .setT(positiveTemp)
                .setPoint(point)
                .setNormal(normal)
                .setMaterial(material)
                .build()
        );
      }
    }

    return Optional.empty();
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return Optional.of(axisAlignedBoundingBox);
  }

}
