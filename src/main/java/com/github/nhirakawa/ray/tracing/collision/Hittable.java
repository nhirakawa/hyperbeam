package com.github.nhirakawa.ray.tracing.collision;

import java.util.Optional;

import com.github.nhirakawa.ray.tracing.geometry.Ray;

public interface Hittable {

  default Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    return Optional.empty();
  }

  default Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return Optional.empty();
  }

}
