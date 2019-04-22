package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectsList;

public final class SceneObjectsListProcessor {

  private SceneObjectsListProcessor() {}

  public static Optional<HitRecord> hit(SceneObjectsList sceneObjectsList, HitRecordParams hitRecordParams) {
    return sceneObjectsList.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }

  public static Optional<AxisAlignedBoundingBox> getBoundingBox(SceneObjectsList sceneObjectsList, BoundingBoxParams boundingBoxParams) {
    return sceneObjectsList.getBoundingBox(boundingBoxParams);
  }
}
