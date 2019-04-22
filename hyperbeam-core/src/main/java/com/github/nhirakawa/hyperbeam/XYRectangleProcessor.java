package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.XYRectangleModel;

public final class XYRectangleProcessor {

  private XYRectangleProcessor() {}

  public static Optional<HitRecord> hit(XYRectangleModel xyRectangleModel, HitRecordParams hitRecordParams) {
    return xyRectangleModel.hit(hitRecordParams.getRay(), hitRecordParams.getTMin(), hitRecordParams.getTMax());
  }
}
