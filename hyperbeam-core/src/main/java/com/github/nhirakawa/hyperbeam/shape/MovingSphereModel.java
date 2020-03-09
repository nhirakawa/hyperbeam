package com.github.nhirakawa.hyperbeam.shape;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.hyperbeam.RayProcessor;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import java.util.Optional;
import org.immutables.value.Value;

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
  public Optional<HitRecord> hit(
    RayProcessor rayProcessor,
    Ray ray,
    double tMin,
    double tMax
  ) {
    return rayProcessor.hitMovingSphere(this, ray, tMin, tMax);
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(
    RayProcessor rayProcessor,
    double t0,
    double t1
  ) {
    return rayProcessor.getBoundingBoxForMovingSphere(this, t0, t1);
  }

  public Vector3 getCenter(double time) {
    double timeMultiplier = (time - getTime0()) / (getTime1() - getTime0());
    return getCenter0()
      .add(getCenter1().subtract(getCenter0()).scalarMultiply(timeMultiplier));
  }
}
