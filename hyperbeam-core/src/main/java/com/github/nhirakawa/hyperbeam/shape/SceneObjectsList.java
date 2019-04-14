package com.github.nhirakawa.hyperbeam.shape;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.google.common.collect.ImmutableList;

public class SceneObjectsList implements SceneObject {

  private static final Logger LOG = LoggerFactory.getLogger(SceneObjectsList.class);

  private final List<ShapeAdt> shapeAdts;
  private final List<? extends SceneObject> hittables;

  public SceneObjectsList(List<ShapeAdt> shapeAdts) {
    this.shapeAdts = ImmutableList.copyOf(shapeAdts);
    this.hittables = shapeAdts.stream()
        .map(this::toSceneObject)
        .collect(ImmutableList.toImmutableList());
  }

  public List<? extends SceneObject> getHittables() {
    return hittables;
  }

  public List<ShapeAdt> getShapeAdts() {
    return shapeAdts;
  }

  @Override
  public SceneObjectType getShapeType() {
    return SceneObjectType.SCENE_OBJECTS_LIST;
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    Optional<HitRecord> tempRecord = Optional.empty();
    double closestSoFar = tMax;

    for (int i = 0; i < shapeAdts.size(); i++) {
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
    if (shapeAdts.isEmpty()) {
      LOG.warn("No shapeAdts");
      return Optional.empty();
    }

    Optional<AxisAlignedBoundingBox> current = hittables.get(0).getBoundingBox(t0, t1);
    if (!current.isPresent()) {
      LOG.warn("Could not get box for first hittable");
      return Optional.empty();
    }

    for (int i = 0; i < shapeAdts.size(); i++) {
      Optional<AxisAlignedBoundingBox> temp = hittables.get(i).getBoundingBox(t0, t1);
      if (!temp.isPresent()) {
        LOG.warn("No bounding box for {}", hittables.get(i));
        return Optional.empty();
      }

      current = Optional.of(AxisAlignedBoundingBox.getSurroundingBox(current.get(), temp.get()));
    }

    return current;
  }

  private SceneObject toSceneObject(ShapeAdt shapeAdt) {
    return ShapeAdts.caseOf(shapeAdt)
        .BOUNDING_VOLUME_HIERARCHY(this::toSceneObject)
        .BOX(this::toSceneObject)
        .CONSTANT_MEDIUM(this::toSceneObject)
        .MOVING_SPHERE(this::toSceneObject)
        .REVERSE_NORMALS(this::toSceneObject)
        .SCENE_OBJECTS_LIST(this::toSceneObject)
        .SPHERE(this::toSceneObject)
        .TRANSLATION(this::toSceneObject)
        .XY_RECTANGLE(this::toSceneObject)
        .XZ_RECTANGLE(this::toSceneObject)
        .YZ_RECTANGLE(this::toSceneObject)
        .Y_ROTATION(this::toSceneObject);
  }

  private <T extends SceneObject> SceneObject toSceneObject(T t) {
    return t;
  }
}
