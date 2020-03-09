package com.github.nhirakawa.hyperbeam.shape;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.RayProcessor;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SceneObjectsList implements SceneObject {
  private static final Logger LOG = LoggerFactory.getLogger(
    SceneObjectsList.class
  );

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
  public Optional<HitRecord> hit(
    RayProcessor rayProcessor,
    Ray ray,
    double tMin,
    double tMax
  ) {
    return rayProcessor.hitSceneObjectsList(this, ray, tMin, tMax);
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(
    RayProcessor rayProcessor,
    double t0,
    double t1
  ) {
    return rayProcessor.getBoundingBoxForSceneObjectsList(this, t0, t1);
  }
}
