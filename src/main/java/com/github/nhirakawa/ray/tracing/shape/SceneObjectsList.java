package com.github.nhirakawa.ray.tracing.shape;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.google.common.collect.ImmutableList;

public class SceneObjectsList implements SceneObject {

  private static final Logger LOG = LoggerFactory.getLogger(SceneObjectsList.class);

  private final List<? extends SceneObject> hittables;

  public SceneObjectsList(List<? extends SceneObject> hittables) {
    this.hittables = ImmutableList.copyOf(hittables);
  }

  public List<? extends SceneObject> getHittables() {
    return hittables;
  }

  @Override
  public SceneObjectType getShapeType() {
    return SceneObjectType.SCENE_OBJECTS_LIST;
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
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

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    if (hittables.isEmpty()) {
      LOG.warn("No hittables");
      return Optional.empty();
    }

    Optional<AxisAlignedBoundingBox> current = hittables.get(0).getBoundingBox(t0, t1);
    if (!current.isPresent()) {
      LOG.warn("Could not get box for first hittable");
      return Optional.empty();
    }

    for (int i = 0; i < hittables.size(); i++) {
      Optional<AxisAlignedBoundingBox> temp = hittables.get(i).getBoundingBox(t0, t1);
      if (!temp.isPresent()) {
        LOG.warn("No bounding box for {}", hittables.get(i));
        return Optional.empty();
      }

      current = Optional.of(AxisAlignedBoundingBox.getSurroundingBox(current.get(), temp.get()));
    }

    return current;
  }

}
