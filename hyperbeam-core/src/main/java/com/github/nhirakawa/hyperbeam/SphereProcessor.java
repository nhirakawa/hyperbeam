package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.SphereModel;

public final class SphereProcessor {

  private SphereProcessor() {}

  public static Optional<HitRecord> hit(SphereModel sphereModel, HitRecordParams hitRecordParams) {
    return sphereModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }
}
