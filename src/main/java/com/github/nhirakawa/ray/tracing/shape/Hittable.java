package com.github.nhirakawa.ray.tracing.shape;

import java.util.Optional;

import com.github.nhirakawa.ray.tracing.geometry.Ray;

public interface Hittable {

  default Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    return Optional.empty();
  }
}
