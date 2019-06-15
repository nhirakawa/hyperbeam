package com.github.nhirakawa.hyperbeam.transform;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectType;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@ImmutableStyle
@Value.Immutable
public abstract class YRotationModel implements SceneObject {

  public abstract SceneObject getSceneObject();
  public abstract double getAngleInDegrees();

  @Value.Derived
  @JsonIgnore
  public double getAngleInRadians() {
    return (Math.PI / 180) * getAngleInDegrees();
  }

  @Value.Derived
  @JsonIgnore
  public double getSinTheta() {
    return Math.sin(getAngleInRadians());
  }

  @Value.Derived
  @JsonIgnore
  public double getCosTheta() {
    return Math.cos(getAngleInRadians());
  }

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.Y_ROTATION;
  }

}
