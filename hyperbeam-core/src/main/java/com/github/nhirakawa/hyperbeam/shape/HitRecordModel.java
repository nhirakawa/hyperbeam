package com.github.nhirakawa.hyperbeam.shape;

import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface HitRecordModel {
  double getT();
  Vector3 getPoint();
  Vector3 getNormal();
  Material getMaterial();

  @Value.Default
  default double getU() {
    return 0;
  }

  @Value.Default
  default double getV() {
    return 0;
  }
}
