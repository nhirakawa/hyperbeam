package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.transform.YRotationModel;

public final class YRotationProcessor {

  private YRotationProcessor() {}

  public static Optional<HitRecord> hit(YRotationModel yRotationModel, HitRecordParams hitRecordParams) {
    return yRotationModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }

  public static Optional<AxisAlignedBoundingBox> getBoundingBox(YRotationModel yRotationModel, BoundingBoxParams boundingBoxParams) {
    return yRotationModel.getBoundingBox(boundingBoxParams);
  }

}
