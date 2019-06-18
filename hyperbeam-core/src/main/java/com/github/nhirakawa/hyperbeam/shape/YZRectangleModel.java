package com.github.nhirakawa.hyperbeam.shape;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @Value.Lazy
  @JsonIgnore
  public Vector3 getNormal() {
    return NORMAL;
  }

  @Value.Lazy
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
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.YZ_RECTANGLE;
  }

}
