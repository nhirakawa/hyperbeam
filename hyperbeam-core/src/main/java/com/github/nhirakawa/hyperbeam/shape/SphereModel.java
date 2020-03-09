package com.github.nhirakawa.hyperbeam.shape;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.hyperbeam.RayProcessor;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public abstract class SphereModel implements SceneObject {

  public abstract Vector3 getCenter();

  public abstract double getRadius();

  public abstract Material getMaterial();

  @JsonIgnore
  @Value.Derived
  public Vector3 getRadiusVector() {
    return Vector3
      .builder()
      .setX(getRadius())
      .setY(getRadius())
      .setZ(getRadius())
      .build();
  }

  @JsonIgnore
  @Value.Derived
  public AxisAlignedBoundingBox getAxisAlignedBoundingBox() {
    return AxisAlignedBoundingBox
      .builder()
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
  public Optional<HitRecord> hit(
    RayProcessor rayProcessor,
    Ray ray,
    double tMin,
    double tMax
  ) {
    return rayProcessor.hitSphere(this, ray, tMin, tMax);
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(
    RayProcessor rayProcessor,
    double t0,
    double t1
  ) {
    return rayProcessor.getBoundingBoxForSphere(this, t0, t1);
  }
}
