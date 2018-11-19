package com.github.nhirakawa.ray.tracing.geometry;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface RayModel {

  Vector3 getOrigin();
  Vector3 getDirection();
  double getTime();

  default Vector3 getPointAtParameter(double t) {
    return getOrigin().add(getDirection().scalarMultiply(t));
  }
}
