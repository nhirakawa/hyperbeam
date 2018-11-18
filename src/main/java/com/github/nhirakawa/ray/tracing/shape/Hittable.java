package com.github.nhirakawa.ray.tracing.shape;

import java.util.Optional;

import com.github.nhirakawa.ray.tracing.geometry.RayModel;

public interface Hittable {

  default Optional<HitRecord> hit(RayModel ray, double tMin, double tMax) {
    return Optional.empty();
  }
}
