package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.XZRectangleModel;

public final class XZRectangleProcessor {

  private XZRectangleProcessor() {}

  public static Optional<HitRecord> hit(XZRectangleModel xzRectangleModel, HitRecordParams hitRecordParams) {
    return xzRectangleModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }
}
