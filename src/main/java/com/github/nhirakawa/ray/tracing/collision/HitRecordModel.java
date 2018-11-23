package com.github.nhirakawa.ray.tracing.collision;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.material.Material;

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
