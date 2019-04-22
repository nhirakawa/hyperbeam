package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.ReverseNormalsModel;

public final class ReverseNormalsProcessor {

  private ReverseNormalsProcessor() {}

  public static Optional<HitRecord> hit(ReverseNormalsModel reverseNormalsModel, HitRecordParams hitRecordParams) {
    return reverseNormalsModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }
}
