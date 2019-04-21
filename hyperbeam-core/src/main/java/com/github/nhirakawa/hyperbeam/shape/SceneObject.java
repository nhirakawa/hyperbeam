package com.github.nhirakawa.hyperbeam.shape;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.geometry.Ray;

public interface SceneObject {

  @SuppressWarnings("unused")
  SceneObjectType getShapeType();

  default Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    return Optional.empty();
  }

  default Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return Optional.empty();
  }

}
