package com.github.nhirakawa.ray.tracing.shape;

import com.github.nhirakawa.ray.tracing.geometry.Vector3;

public class HitRecord {

  private final double t;
  private final Vector3 point;
  private final Vector3 normal;

  public HitRecord(double t, Vector3 point, Vector3 normal) {
    this.t = t;
    this.point = point;
    this.normal = normal;
  }

  public double getT() {
    return t;
  }

  public Vector3 getPoint() {
    return point;
  }

  public Vector3 getNormal() {
    return normal;
  }
}
