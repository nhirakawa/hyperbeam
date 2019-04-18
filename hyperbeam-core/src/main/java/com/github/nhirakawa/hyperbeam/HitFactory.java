package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.ShapeAdt;

public final class HitFactory {

  private HitFactory() {}

  public static Optional<HitFactory> hit(ShapeAdt shapeAdt, HitRecordParams hitRecordParams) {
    throw new UnsupportedOperationException();
  }

}
