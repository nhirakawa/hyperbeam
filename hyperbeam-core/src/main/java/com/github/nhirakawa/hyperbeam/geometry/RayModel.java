package com.github.nhirakawa.hyperbeam.geometry;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import org.immutables.value.Value;

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
