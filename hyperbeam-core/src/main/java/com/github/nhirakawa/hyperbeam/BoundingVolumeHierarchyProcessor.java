package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.BoundingVolumeHierarchyModel;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;

public final class BoundingVolumeHierarchyProcessor {

  private BoundingVolumeHierarchyProcessor() {}

  public static Optional<HitRecord> hit(BoundingVolumeHierarchyModel boundingVolumeHierarchyModel, HitRecordParams hitRecordParams) {
    return boundingVolumeHierarchyModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }

}
