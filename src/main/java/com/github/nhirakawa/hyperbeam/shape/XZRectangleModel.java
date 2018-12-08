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
public abstract class XZRectangleModel implements SceneObject {

  public abstract double getX0();
  public abstract double getX1();
  public abstract double getZ0();
  public abstract double getZ1();
  public abstract double getK();
  public abstract Material getMaterial();

  @Value.Lazy
  @JsonIgnore
  public AxisAlignedBoundingBox getBoundingBox() {
    return AxisAlignedBoundingBox.builder()
        .setMin(new Vector3(getX0(), getK() - 0.0001, getZ0()))
        .setMax(new Vector3(getX1(), getK() + 0.0001, getZ1()))
        .build();
  }

  @Value.Lazy
  @JsonIgnore
  public Vector3 getNormal() {
    return new Vector3(0, 1, 0);
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    double t = (getK() - ray.getOrigin().getY()) / ray.getDirection().getY();

    if (t < tMin || t > tMax) {
      return Optional.empty();
    }

    double x = ray.getOrigin().getX() + (t * ray.getDirection().getX());
    double z = ray.getOrigin().getZ() + (t * ray.getDirection().getZ());

    if (x < getX0() || x > getX1() || z < getZ0() || z > getZ1()) {
      return Optional.empty();
    }

    double u = (x - getX0()) / (getX1() - getX0());
    double v = (z - getZ0()) / (getZ1() - getZ0());

    return Optional.of(
        HitRecord.builder()
            .setPoint(ray.getPointAtParameter(t))
            .setT(t)
            .setU(u)
            .setV(v)
            .setNormal(getNormal())
            .setMaterial(getMaterial())
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
    return SceneObjectType.XZ_RECTANGLE;
  }

}
