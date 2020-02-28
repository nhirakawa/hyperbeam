package com.github.nhirakawa.hyperbeam.shape;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public abstract class XZRectangleModel implements SceneObject {
  private static final Vector3 NORMAL = Vector3
    .builder()
    .setX(0)
    .setY(1)
    .setZ(0)
    .build();

  public abstract double getX0();

  public abstract double getX1();

  public abstract double getZ0();

  public abstract double getZ1();

  public abstract double getK();

  public abstract Material getMaterial();

  @Value.Lazy
  @JsonIgnore
  public AxisAlignedBoundingBox getBoundingBox() {
    Vector3 min = Vector3
      .builder()
      .setX(getX0())
      .setY(getK() - 0.0001)
      .setZ(getZ0())
      .build();

    Vector3 max = Vector3
      .builder()
      .setX(getX1())
      .setY(getK() + 0.0001)
      .setZ(getZ1())
      .build();

    return AxisAlignedBoundingBox.builder().setMin(min).setMax(max).build();
  }

  @Value.Lazy
  @JsonIgnore
  public Vector3 getNormal() {
    return NORMAL;
  }

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.XZ_RECTANGLE;
  }
}
