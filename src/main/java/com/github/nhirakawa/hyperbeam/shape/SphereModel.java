package com.github.nhirakawa.hyperbeam.shape;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.google.common.collect.Range;

@Value.Immutable
@ImmutableStyle
public abstract class SphereModel implements SceneObject {

  public abstract Vector3 getCenter();
  public abstract double getRadius();
  public abstract Material getMaterial();

  @JsonIgnore
  @Value.Derived
  public Vector3 getRadiusVector() {
    return new Vector3(getRadius(), getRadius(), getRadius());
  }

  @JsonIgnore
  @Value.Derived
  public AxisAlignedBoundingBox getAxisAlignedBoundingBox() {
    return AxisAlignedBoundingBox.builder()
        .setMin(getCenter().subtract(getRadiusVector()))
        .setMax(getCenter().add(getRadiusVector()))
        .build();
  }

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.SPHERE;
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    Vector3 oc = ray.getOrigin().subtract(getCenter());

    double a = ray.getDirection().dotProduct(ray.getDirection());
    double b = oc.dotProduct(ray.getDirection());
    double c = oc.dotProduct(oc) - (getRadius() * getRadius());

    double discriminant = (b * b) - (a * c);

    if (discriminant > 0) {
      double negativeTemp = (-b - Math.sqrt(discriminant)) / a; // TODO rename this awful variable
      Range<Double> parameterRange = Range.open(tMin, tMax);

      if (parameterRange.contains(negativeTemp)) {
        Vector3 point = ray.getPointAtParameter(negativeTemp);
        Vector3 normal = point.subtract(getCenter()).scalarDivide(getRadius());

        Vector3 uvPoint = point.subtract(getCenter());

        double phi = Math.atan2(uvPoint.getZ(), uvPoint.getX());
        double theta = Math.asin(uvPoint.getY());

        double u = (1 - phi + Math.PI) / (2 * Math.PI);
        double v = (theta + (Math.PI / 2)) / Math.PI;

        return Optional.of(
            HitRecord.builder()
                .setT(negativeTemp)
                .setPoint(point)
                .setNormal(normal)
                .setMaterial(getMaterial())
                .setU(u)
                .setV(v)
                .build()
        );
      }

      double positiveTemp = (-b + Math.sqrt(discriminant)) / a; // TODO rename this awful variable
      if (parameterRange.contains(positiveTemp)) {
        Vector3 point = ray.getPointAtParameter(positiveTemp);
        Vector3 normal = point.subtract(getCenter()).scalarDivide(getRadius());

        Vector3 uvPpoint = point.subtract(getCenter());

        double phi = Math.atan2(uvPpoint.getZ(), uvPpoint.getX());
        double theta = Math.asin(uvPpoint.getY());

        double u = (1 - phi + Math.PI) / (2 * Math.PI);
        double v = (theta + (Math.PI / 2)) / Math.PI;

        return Optional.of(
            HitRecord.builder()
                .setT(positiveTemp)
                .setPoint(point)
                .setNormal(normal)
                .setMaterial(getMaterial())
                .setU(u)
                .setV(v)
                .build()
        );
      }
    }

    return Optional.empty();
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return Optional.of(getAxisAlignedBoundingBox());
  }

}
