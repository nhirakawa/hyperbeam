package com.github.nhirakawa.hyperbeam.shape;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.AlgebraicSceneObject;
import com.github.nhirakawa.hyperbeam.AlgebraicSceneObjects;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public abstract class SphereModel implements SceneObject {

  public abstract Vector3 getCenter();
  public abstract double getRadius();
  public abstract Material getMaterial();

  @JsonIgnore
  @Value.Derived
  public Vector3 getRadiusVector() {
    return Vector3.builder()
        .setX(getRadius())
        .setY(getRadius())
        .setZ(getRadius())
        .build();
  }

  @JsonIgnore
  @Value.Derived
  public AxisAlignedBoundingBox getAxisAlignedBoundingBox() {
    return AxisAlignedBoundingBox.builder()
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
  @JsonIgnore
  @Value.Lazy
  public AlgebraicSceneObject toAlgebraicSceneObject() {
    return AlgebraicSceneObjects.SPHERE(this);
  }

}
