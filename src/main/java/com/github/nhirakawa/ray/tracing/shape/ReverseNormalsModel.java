package com.github.nhirakawa.ray.tracing.shape;

import java.util.Optional;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.geometry.Ray;

@Value.Immutable
@ImmutableStyle
public abstract class ReverseNormalsModel implements SceneObject {

  public abstract SceneObject getSceneObject();

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    return getSceneObject().hit(ray, tMin, tMax)
        .map(hitRecord -> hitRecord.withNormal(hitRecord.getNormal().negate()));
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return getSceneObject().getBoundingBox(t0, t1);
  }

  @Override
  public SceneObjectType getShapeType() {
    return SceneObjectType.REVERSE_NORMALS;
  }

}
