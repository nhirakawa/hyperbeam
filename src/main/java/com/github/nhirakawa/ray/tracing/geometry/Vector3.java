package com.github.nhirakawa.ray.tracing.geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Vector3 extends Vector3D {

  public Vector3(double x, double y, double z) {
    super(x, y, z);
  }

  public double getRed() {
    return getX();
  }

  public double getGreen() {
    return getY();
  }

  public double getBlue() {
    return getZ();
  }

  public Vector3 add(Vector3 vector) {
    return from(super.add(vector));
  }

  public Vector3 multiply(Vector3 vector) {
    double x = getX() * vector.getX();
    double y = getY() * vector.getY();
    double z = getZ() * vector.getZ();

    return new Vector3(x, y, z);
  }

  public Vector3 divide(Vector3 vector) {
    double x = getX() / vector.getX();
    double y = getY() / vector.getY();
    double z = getZ() / vector.getZ();

    return new Vector3(x, y, z);
  }

  public Vector3 scalarMultiply(double scalar) {
    return from(super.scalarMultiply(scalar));
  }

  public Vector3 scalarDivide(double scalar, Vector3 vector) {
    return vector.scalarMultiply(1 / scalar);
  }

  public Vector3 cross(Vector3 vector) {
    return from(crossProduct(vector));
  }

  public Vector3 unit() {
    return from(normalize());
  }

  private static Vector3 from(Vector3D vector) {
    return new Vector3(vector.getX(), vector.getY(), vector.getZ());
  }

}
