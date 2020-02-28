package com.github.nhirakawa.hyperbeam.shape;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import java.util.Optional;
import org.immutables.value.Value;

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
    Vector3 min = Vector3
      .builder()
      .setX(getX0())
      .setY(getY0())
      .setZ(getK() - 0.0001)
      .build();

    Vector3 max = Vector3
      .builder()
      .setX(getX1())
      .setY(getY1())
      .setZ(getK() + 0.0001)
      .build();

    return Optional.of(
      AxisAlignedBoundingBox.builder().setMin(min).setMax(max).build()
    );
  }

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.XY_RECTANGLE;
  }
}
