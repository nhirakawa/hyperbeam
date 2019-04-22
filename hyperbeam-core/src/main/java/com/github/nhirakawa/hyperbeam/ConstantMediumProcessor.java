package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.ConstantMediumModel;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;

public final class ConstantMediumProcessor {

  private ConstantMediumProcessor() {}

  public static Optional<HitRecord> hit(ConstantMediumModel constantMediumModel, HitRecordParams hitRecordParams) {
    return constantMediumModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }
}
