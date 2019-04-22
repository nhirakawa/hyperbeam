package com.github.nhirakawa.hyperbeam.shape;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public abstract class YZRectangleModel implements SceneObject {

  private static final Vector3 NORMAL = Vector3.builder()
      .setX(1)
      .setY(0)
      .setZ(0)
      .build();

  public abstract double getY0();
  public abstract double getY1();
  public abstract double getZ0();
  public abstract double getZ1();
  public abstract double getK();
  public abstract Material getMaterial();

  @Value.Derived
  @JsonIgnore
  public Vector3 getNormal() {
    return NORMAL;
  }

  @Value.Derived
  @JsonIgnore
  public AxisAlignedBoundingBox getBoundingBox() {
    Vector3 min = Vector3.builder()
        .setX(getK() - 0.0001)
        .setY(getY0())
        .setZ(getZ0())
        .build();

    Vector3 max = Vector3.builder()
        .setX(getK() + 0.0001)
        .setY(getY1())
        .setZ(getZ1())
        .build();

    return AxisAlignedBoundingBox.builder()
        .setMin(min)
        .setMax(max)
        .build();
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    double t = (getK() - ray.getOrigin().getX()) / ray.getDirection().getX();

    if (t < tMin || t > tMax) {
      return Optional.empty();
    }

    double y = ray.getOrigin().getY() + (t * ray.getDirection().getY());
    double z = ray.getOrigin().getZ() + (t * ray.getDirection().getZ());

    if (y < getY0() || y > getY1() || z < getZ0() || z > getZ1()) {
      return Optional.empty();
    }

    double u = (y - getY0()) / (getY1() - getY0());
    double v = (z - getZ0()) / (getZ1() - getZ0());

    return Optional.of(
        HitRecord.builder()
            .setPoint(ray.getPointAtParameter(t))
            .setMaterial(getMaterial())
            .setT(t)
            .setU(u)
            .setV(v)
            .setNormal(getNormal())
            .build()
    );
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return Optional.of(getBoundingBox());
  }

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.YZ_RECTANGLE;
  }

}
