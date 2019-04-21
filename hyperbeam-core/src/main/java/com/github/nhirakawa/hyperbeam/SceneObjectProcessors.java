package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdt;

public final class SceneObjectProcessors {

  private SceneObjectProcessors() {}

  public static Optional<HitRecord> hit(ShapeAdt shapeAdt, HitRecordParams hitRecordParams) {
    throw new UnsupportedOperationException();
  }

  public static Optional<AxisAlignedBoundingBox> getBoundingBox(ShapeAdt shapeAdt, BoundingBoxParams boundingBoxParams) {
    throw new UnsupportedOperationException();
  }

}
