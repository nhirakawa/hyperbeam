package com.github.nhirakawa.ray.tracing.shape;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.material.Material;

@Value.Immutable
@ImmutableStyle
public abstract class XYRectangleModel implements SceneObject {

  public abstract double getX0();
  public abstract double getX1();
  public abstract double getY0();
  public abstract double getY1();
  public abstract double getK();
  public abstract Material getMaterial();

  @Value.Lazy
  @JsonIgnore
  public Optional<AxisAlignedBoundingBox> getBoundingBox() {
    return Optional.of(
        AxisAlignedBoundingBox.builder()
            .setMin(new Vector3(getX0(), getY0(), getK() - 0.0001))
            .setMax(new Vector3(getX1(), getY1(), getK() + 0.0001))
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

    return Optional.of(
        HitRecord.builder()
            .setPoint(ray.getPointAtParameter(t))
            .setNormal(new Vector3(0, 0, 1))
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
