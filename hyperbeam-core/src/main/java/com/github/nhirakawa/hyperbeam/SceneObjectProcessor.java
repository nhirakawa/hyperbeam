package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;

public interface SceneObjectProcessor {

  Optional<HitRecord> hit(HitRecordParams hitRecordParams);

  Optional<AxisAlignedBoundingBox> getBoundingBox(BoundingBoxParams boundingBoxParams);

}
