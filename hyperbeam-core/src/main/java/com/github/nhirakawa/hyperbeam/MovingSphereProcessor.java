package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.MovingSphereModel;

public final class MovingSphereProcessor {

  private MovingSphereProcessor() {}

  public static Optional<HitRecord> hit(MovingSphereModel movingSphereModel, HitRecordParams hitRecordParams) {
    return movingSphereModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }

  public static Optional<AxisAlignedBoundingBox> getBoundingBox(MovingSphereModel movingSphereModel, BoundingBoxParams boundingBoxParams) {
    return movingSphereModel.getBoundingBox(boundingBoxParams);
  }
}
