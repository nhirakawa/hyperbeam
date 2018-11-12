package com.github.nhirakawa.ray.tracing;

import java.util.Optional;

import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.google.common.collect.Range;

public class Sphere implements Hittable {

  private final Vector3 center;
  private final double radius;

  public Sphere(Vector3 center, double radius) {
    this.center = center;
    this.radius = radius;
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
        return Optional.of(new HitRecord(negativeTemp, point, normal));
      }

      double positiveTemp = (-b + Math.sqrt(discriminant)) / a; // TODO rename this awful variable
      if (parameterRange.contains(positiveTemp)) {
        Vector3 point = ray.getPointAtParameter(positiveTemp);
        Vector3 normal = point.subtract(center).scalarDivide(radius);
        return Optional.of(new HitRecord(positiveTemp, point, normal));
      }
    }

    return Optional.empty();
  }
}