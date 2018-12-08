package com.github.nhirakawa.hyperbeam.geometry;

import java.util.Objects;
import java.util.function.Function;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Vector3 {

  private static final Vector3 ZERO = new Vector3(0, 0, 0);

  private final double x;
  private final double y;
  private final double z;

  public Vector3() {
    this(0, 0, 0);
  }

  public Vector3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  public double get(int i) {
    switch (i) {
      case 0:
        return x;
      case 1:
        return y;
      case 2:
        return z;
      default:
        throw new IllegalArgumentException(i + " is out of bounds");
    }
  }

  @JsonIgnore
  public double getRed() {
    return x;
  }

  @JsonIgnore
  public double getGreen() {
    return y;
  }

  @JsonIgnore
  public double getBlue() {
    return z;
  }

  public Vector3 add(Vector3 vector) {
    return from(new Vector3D(x, y, z).add(new Vector3D(vector.x, vector.y, vector.z)));
  }

  public Vector3 subtract(Vector3 vector) {
    return from(new Vector3D(x, y, z).subtract(new Vector3D(vector.x, vector.y, vector.z)));
  }

  public Vector3 multiply(Vector3 vector) {
    double newX = x * vector.x;
    double newY = y * vector.y;
    double newZ = z * vector.z;

    return new Vector3(newX, newY, newZ);
  }

  public Vector3 divide(Vector3 vector) {
    double newX = x / vector.x;
    double newY = y / vector.y;
    double newZ = z / vector.z;

    return new Vector3(newX, newY, newZ);
  }

  public Vector3 scalarMultiply(double scalar) {
    return from(new Vector3D(x, y, z).scalarMultiply(scalar));
  }

  public Vector3 scalarDivide(double scalar) {
    return scalarMultiply(1 / scalar);
  }

  public double dotProduct(Vector3 vector) {
    return new Vector3D(x, y, z).dotProduct(new Vector3D(vector.x, vector.y, vector.z));
  }

  public Vector3 cross(Vector3 vector) {
    Vector3D cross = new Vector3D(x, y, z).crossProduct(new Vector3D(vector.x, vector.y, vector.z));
    return new Vector3(cross.getX(), cross.getY(), cross.getZ());
  }

  public Vector3 unit() {
    return from(new Vector3D(x, y, z).normalize());
  }

  @JsonIgnore
  public double getSquaredLength() {
    return (x * x) + (y * y) + (z * z);
  }

  @JsonIgnore
  public double getNorm() {
    return new Vector3D(x, y, z).getNorm();
  }

  @JsonIgnore
  public Vector3 negate() {
    return new Vector3(-x, -y, -z);
  }

  public Vector3 apply(Function<Double, Double> function) {
    double newX = function.apply(x);
    double newY = function.apply(y);
    double newZ = function.apply(z);

    return new Vector3(newX, newY, newZ);
  }

  public static Vector3 zero() {
    return ZERO;
  }

  private static Vector3 from(Vector3D vector) {
    return new Vector3(vector.getX(), vector.getY(), vector.getZ());
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }

    if (!(o instanceof Vector3)) {
      return false;
    }

    if (o == this) {
      return true;
    }

    Vector3 other = (Vector3) o;

    return x == other.x && y == other.y && z == other.z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

}
