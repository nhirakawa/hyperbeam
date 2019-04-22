package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.BoxModel;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;

public final class BoxProcessor {

  private BoxProcessor() {}

  public static Optional<HitRecord> hit(BoxModel boxModel, HitRecordParams hitRecordParams) {
    return boxModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }

}
