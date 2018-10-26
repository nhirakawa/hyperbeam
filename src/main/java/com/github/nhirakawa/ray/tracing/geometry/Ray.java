package com.github.nhirakawa.ray.tracing.geometry;

public class Ray {

  private final Vector3 origin;
  private final Vector3 direction;

  public Ray(Vector3 origin,
             Vector3 direction) {
    this.origin = origin;
    this.direction = direction;
  }

  public Vector3 getOrigin() {
    return origin;
  }

  public Vector3 getDirection() {
    return direction;
  }

  public Vector3 getPointAtParameter(double t) {
    return origin.add(direction.scalarMultiply(t));
  }
}
