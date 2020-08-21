package com.github.nhirakawa.hyperbeam.geometry;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface RayModel {
  Vector3 getOrigin();
  Vector3 getDirection();
  double getTime();

  default Vector3 getPointAtParameter(double t) {
    return Vector3.builder()
        .setX(getOrigin().getX() + (getDirection().getX() * t))
        .setY(getOrigin().getY() + (getDirection().getY() * t))
        .setZ(getOrigin().getZ() + (getDirection().getZ() * t))
        .build();
  }
}
