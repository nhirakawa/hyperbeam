package com.github.nhirakawa.ray.tracing.shape;

import java.util.List;
import java.util.Optional;

import com.github.nhirakawa.ray.tracing.geometry.RayModel;

public class HittablesList implements Hittable {

  private final List<Hittable> hittables;

  public HittablesList(List<Hittable> hittables) {
    this.hittables = hittables;
  }

  @Override
  public Optional<HitRecord> hit(RayModel ray, double tMin, double tMax) {
    Optional<HitRecord> tempRecord = Optional.empty();
    double closestSoFar = tMax;

    for (int i = 0; i < hittables.size(); i++) {
      Optional<HitRecord> maybeHitRecord = hittables.get(i).hit(ray, tMin, closestSoFar);
      if (maybeHitRecord.isPresent()) {
        tempRecord = maybeHitRecord;
        closestSoFar = maybeHitRecord.get().getT();
      }
    }

    return tempRecord;
  }
}
