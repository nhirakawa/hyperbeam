package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.BoxModel;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;

public final class BoxProcessor {

  private BoxProcessor() {}

  public static Optional<HitRecord> hit(BoxModel boxModel, HitRecordParams hitRecordParams) {
    return boxModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }

  public static Optional<AxisAlignedBoundingBox> getBoundingBox(BoxModel boxModel, BoundingBoxParams boundingBoxParams) {
    return boxModel.getBoundingBox(boundingBoxParams);
  }

}
