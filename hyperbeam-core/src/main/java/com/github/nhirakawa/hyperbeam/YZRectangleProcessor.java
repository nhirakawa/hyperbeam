package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.YZRectangleModel;

public final class YZRectangleProcessor {

  private YZRectangleProcessor() {}

  public static Optional<HitRecord> hit(YZRectangleModel yzRectangleModel, HitRecordParams hitRecordParams) {
    return yzRectangleModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }
}
