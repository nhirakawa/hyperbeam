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
public abstract class MovingSphereModel implements SceneObject {

  public abstract Vector3 getCenter0();
  public abstract Vector3 getCenter1();
  public abstract double getTime0();
  public abstract double getTime1();
  public abstract double getRadius();
  public abstract Material getMaterial();

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.MOVING_SPHERE;
  }

  @Override
  @Value.Lazy
  @JsonIgnore
  public AlgebraicSceneObject toAlgebraicSceneObject() {
    return AlgebraicSceneObjects.MOVING_SPHERE(this);
  }

  public Vector3 getCenter(double time) {
    double timeMultiplier = (time - getTime0()) / (getTime1() - getTime0());
    return getCenter0().add(getCenter1().subtract(getCenter0()).scalarMultiply(timeMultiplier));
  }

}
