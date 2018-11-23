package com.github.nhirakawa.ray.tracing.shape;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.collision.AxisAlignedBoundingBox;
import com.github.nhirakawa.ray.tracing.collision.HitRecord;
import com.github.nhirakawa.ray.tracing.collision.HittablesList;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.material.Material;
import com.google.common.collect.ImmutableList;

@Value.Immutable
@ImmutableStyle
public abstract class BoxModel implements Shape {

  public abstract Vector3 getPMin();
  public abstract Vector3 getPMax();
  public abstract Material getMaterial();

  @Value.Lazy
  @JsonIgnore
  public HittablesList getHittablesList() {
    return new HittablesList(
        ImmutableList.of(
            XYRectangle.builder() // 0
                .setX0(getPMin().getX())
                .setX1(getPMax().getX())
                .setY0(getPMin().getY())
                .setY1(getPMax().getY())
                .setK(getPMax().getZ())
                .setMaterial(getMaterial())
                .build(),
            ReverseNormals.builder() // 1
                .setShape(
                    XYRectangle.builder()
                        .setX0(getPMin().getX())
                        .setX1(getPMax().getX())
                        .setY0(getPMin().getY())
                        .setY1(getPMax().getY())
                        .setK(getPMin().getZ())
                        .setMaterial(getMaterial())
                        .build()
                )
                .build(),
            XZRectangle.builder() // 2
                .setX0(getPMin().getX())
                .setX1(getPMax().getX())
                .setZ0(getPMin().getZ())
                .setZ1(getPMax().getZ())
                .setK(getPMax().getY())
                .setMaterial(getMaterial())
                .build(),
            ReverseNormals.builder() // 3
                .setShape(
                    XZRectangle.builder()
                        .setX0(getPMin().getX())
                        .setX1(getPMax().getX())
                        .setZ0(getPMin().getZ())
                        .setZ1(getPMax().getZ())
                        .setK(getPMin().getY())
                        .setMaterial(getMaterial())
                        .build()
                )
                .build(),
            YZRectangle.builder() // 4
                .setY0(getPMin().getY())
                .setY1(getPMax().getY())
                .setZ0(getPMin().getZ())
                .setZ1(getPMax().getZ())
                .setK(getPMax().getX())
                .setMaterial(getMaterial())
                .build(),
            ReverseNormals.builder() // 5
                .setShape(
                    YZRectangle.builder()
                        .setY0(getPMin().getY())
                        .setY1(getPMax().getY())
                        .setZ0(getPMin().getZ())
                        .setZ1(getPMax().getZ())
                        .setK(getPMin().getX())
                        .setMaterial(getMaterial())
                        .build()
                )
                .build()
        )
    );
  }

  @Value.Lazy
  @JsonIgnore
  public AxisAlignedBoundingBox getBoundingBox() {
    return AxisAlignedBoundingBox.builder()
        .setMin(getPMin())
        .setMax(getPMax())
        .build();
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    return getHittablesList().hit(ray, tMin, tMax);
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return Optional.of(getBoundingBox());
  }

  @Override
  @Value.Auxiliary
  public ShapeType getShapeType() {
    return ShapeType.BOX;
  }

}
