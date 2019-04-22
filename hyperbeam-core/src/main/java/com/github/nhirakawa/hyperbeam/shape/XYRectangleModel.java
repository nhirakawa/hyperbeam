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
public abstract class XYRectangleModel implements SceneObject {

  public abstract double getX0();
  public abstract double getX1();
  public abstract double getY0();
  public abstract double getY1();
  public abstract double getK();
  public abstract Material getMaterial();

  @Value.Derived
  @JsonIgnore
  public Optional<AxisAlignedBoundingBox> getBoundingBox() {
    Vector3 min = Vector3.builder()
        .setX(getX0())
        .setY(getY0())
        .setZ(getK() - 0.0001)
        .build();

    Vector3 max = Vector3.builder()
        .setX(getX1())
        .setY(getY1())
        .setZ(getK() + 0.0001)
        .build();

    return Optional.of(
        AxisAlignedBoundingBox.builder()
            .setMin(min)
            .setMax(max)
            .build()
    );
  }

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.XY_RECTANGLE;
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    double t = (getK() - ray.getOrigin().getZ()) / ray.getDirection().getZ();

    if (t < tMin || t > tMax) {
      return Optional.empty();
    }

    double x = ray.getOrigin().getX() + (ray.getDirection().getX() * t);
    double y = ray.getOrigin().getY() + (ray.getDirection().getY() * t);

    if (x < getX0() || x > getX1() || y < getY0() || y > getY1()) {
      return Optional.empty();
    }

    double u = (x - getX0()) / (getX1() - getX0());
    double v = (y - getY0()) / (getY1() - getY0());

    Vector3 normal = Vector3.builder()
        .setX(0)
        .setY(0)
        .setZ(1)
        .build();

    return Optional.of(
        HitRecord.builder()
            .setPoint(ray.getPointAtParameter(t))
            .setNormal(normal)
            .setMaterial(getMaterial())
            .setT(t)
            .setU(u)
            .setV(v)
            .build()
    );
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return getBoundingBox();
  }

}
