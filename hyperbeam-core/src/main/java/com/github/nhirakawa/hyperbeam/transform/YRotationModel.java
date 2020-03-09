package com.github.nhirakawa.hyperbeam.transform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.RayProcessor;
import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectType;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import java.util.Optional;
import org.immutables.value.Value;

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

  @Override
  public Optional<HitRecord> hit(
    RayProcessor rayProcessor,
    Ray ray,
    double tMin,
    double tMax
  ) {
    return rayProcessor.hitYRotation(this, ray, tMin, tMax);
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(
    RayProcessor rayProcessor,
    double t0,
    double t1
  ) {
    return rayProcessor.getBoundingBoxForYRotation(this, t0, t1);
  }
}
