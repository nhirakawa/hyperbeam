package com.github.nhirakawa.ray.tracing.geometry;

import java.util.function.Function;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Vector3 extends Vector3D {

  private static final Vector3 ZERO = new Vector3(0, 0, 0);

  public Vector3(double x, double y, double z) {
    super(x, y, z);
  }

  public double get(int i) {
    switch (i) {
      case 0:
        return getX();
      case 1:
        return getY();
      case 2:
        return getZ();
      default:
        throw new IllegalArgumentException(i + " is out of bounds");
    }
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

  public Vector3 subtract(Vector3 vector) {
    return from(super.subtract(vector));
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

  public Vector3 scalarDivide(double scalar) {
    return scalarMultiply(1 / scalar);
  }

  public Vector3 cross(Vector3 vector) {
    return from(crossProduct(vector));
  }

  public Vector3 unit() {
    return from(normalize());
  }

  public double getSquaredLength() {
    return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ());
  }

  public Vector3 apply(Function<Double, Double> function) {
    double newX = function.apply(getX());
    double newY = function.apply(getY());
    double newZ = function.apply(getZ());

    return new Vector3(newX, newY, newZ);
  }

  public static Vector3 zero() {
    return ZERO;
  }

  private static Vector3 from(Vector3D vector) {
    return new Vector3(vector.getX(), vector.getY(), vector.getZ());
  }

}
