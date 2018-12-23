package com.github.nhirakawa.hyperbeam.transform;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectType;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

@ImmutableStyle
@Value.Immutable
public abstract class YRotationModel implements SceneObject {

  public abstract SceneObject getSceneObject();
  public abstract double getAngleInDegrees();

  @Value.Derived
  @JsonIgnore
  public double getAngleInRadians() {
    return (Math.PI / 180) * getAngleInDegrees();
  }

  @Value.Derived
  @JsonIgnore
  public double getSinTheta() {
    return Math.sin(getAngleInRadians());
  }

  @Value.Derived
  @JsonIgnore
  public double getCosTheta() {
    return Math.cos(getAngleInRadians());
  }

  @Value.Derived
  @JsonIgnore
  public Optional<AxisAlignedBoundingBox> getBoundingBox() {
    Optional<AxisAlignedBoundingBox> maybeBoundingBox = getSceneObject().getBoundingBox(0, 1);
    if (!maybeBoundingBox.isPresent()) {
      return Optional.empty();
    }

    AxisAlignedBoundingBox boundingBox = maybeBoundingBox.get();

    Vector3 min = Vector3.max();
    Vector3 max = Vector3.min();

    Set<Integer> integers = ImmutableSet.of(0, 1);
    Set<List<Integer>> products = Sets.cartesianProduct(integers, integers, integers);

    for (List<Integer> product : products) {
      Preconditions.checkState(product.size() == 3);

      int i = product.get(0);
      int j = product.get(1);
      int k = product.get(2);

      double x = (i * boundingBox.getMax().getX()) + ((1 - i) * boundingBox.getMin().getX());
      double y = (j * boundingBox.getMax().getY()) + ((1 - j) * boundingBox.getMin().getY());
      double z = (k * boundingBox.getMax().getZ()) + ((1 - k) * boundingBox.getMin().getZ());

      double newX = (getCosTheta() * x) + (getSinTheta() * z);
      double newZ = (getCosTheta() * z) - (getSinTheta() * x);

      Vector3 tester = Vector3.builder()
          .setX(newX)
          .setY(y)
          .setZ(newZ)
          .build();

      double maxX = Math.max(tester.getX(), max.getX());
      double maxY = Math.max(tester.getY(), max.getY());
      double maxZ = Math.max(tester.getZ(), max.getZ());

      max = Vector3.builder()
          .setX(maxX)
          .setY(maxY)
          .setZ(maxZ)
          .build();

      double minX = Math.min(tester.getX(), min.getX());
      double minY = Math.min(tester.getY(), min.getY());
      double minZ = Math.min(tester.getZ(), min.getZ());

      min = Vector3.builder()
          .setX(minX)
          .setY(minY)
          .setZ(minZ)
          .build();
    }

    return Optional.of(
        AxisAlignedBoundingBox.builder()
            .setMin(min)
            .setMax(max)
            .build()
    );
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    Vector3 origin = ray.getOrigin();
    Vector3 direction = ray.getDirection();

    double newOriginX = (getCosTheta() * origin.getX()) - (getSinTheta() * origin.getZ());
    double newOriginZ = (getSinTheta() * origin.getX()) + (getCosTheta() * origin.getZ());

    double newDirectionX = (getCosTheta() * direction.getX()) - (getSinTheta() * direction.getZ());
    double newDirectionZ = (getSinTheta() * direction.getX()) + (getCosTheta() * direction.getZ());

    Vector3 newOrigin = Vector3.builder()
        .setX(newOriginX)
        .setY(origin.getY())
        .setZ(newOriginZ)
        .build();

    Vector3 newDirection = Vector3.builder()
        .setX(newDirectionX)
        .setY(direction.getY())
        .setZ(newDirectionZ)
        .build();

    Ray rotatedRay = Ray.builder()
        .from(ray)
        .setOrigin(newOrigin)
        .setDirection(newDirection)
        .build();

    Optional<HitRecord> maybeHitRecord = getSceneObject().hit(rotatedRay, tMin, tMax);
    if (!maybeHitRecord.isPresent()) {
      return Optional.empty();
    }

    HitRecord hitRecord = maybeHitRecord.get();
    Vector3 point = hitRecord.getPoint();
    Vector3 normal = hitRecord.getNormal();

    double newPointX = (getCosTheta() * point.getX()) + (getSinTheta() * point.getZ());
    double newPointZ = (getCosTheta() * point.getZ()) - (getSinTheta() * point.getX());

    double newNormalX = (getCosTheta() * normal.getX()) + (getSinTheta() * normal.getZ());
    double newNormalZ = (getCosTheta() * normal.getZ()) - (getSinTheta() * normal.getZ());

    Vector3 newPoint = Vector3.builder()
        .setX(newPointX)
        .setY(point.getY())
        .setZ(newPointZ)
        .build();

    Vector3 newNormal = Vector3.builder()
        .setX(newNormalX)
        .setY(normal.getY())
        .setZ(newNormalZ)
        .build();

    return Optional.of(
        HitRecord.builder()
            .from(hitRecord)
            .setPoint(newPoint)
            .setNormal(newNormal)
            .build()
    );
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return getBoundingBox();
  }

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.Y_ROTATION;
  }

}
