package com.github.nhirakawa.hyperbeam.geometry;

import java.util.function.Function;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public abstract class Vector3Model {

  private static final Vector3 ZERO = Vector3.builder()
      .setX(0)
      .setY(0)
      .setZ(0)
      .build();

  private static final Vector3 MAX = Vector3.builder()
      .setX(Double.MAX_VALUE)
      .setY(Double.MAX_VALUE)
      .setZ(Double.MAX_VALUE)
      .build();

  private static final Vector3 MIN = Vector3.builder()
      .setX(-Double.MAX_VALUE)
      .setY(-Double.MAX_VALUE)
      .setZ(-Double.MAX_VALUE)
      .build();

  private static final Vector3 ONE = Vector3.builder()
      .setX(1)
      .setY(1)
      .setZ(1)
      .build();

  public abstract double getX();
  public abstract double getY();
  public abstract double getZ();

  public double get(int i) {
    switch (i) {
      case 0:
        return getX();
      case 1:
        return getY();
      case 2:
        return getZ();
      default:
        throw new IllegalArgumentException(String.format("%s is not in [0,2]", i));
    }
  }

  @JsonIgnore
  @Value.Auxiliary
  public double getRed() {
    return getX();
  }

  @JsonIgnore
  @Value.Auxiliary
  public double getGreen() {
    return getY();
  }

  @JsonIgnore
  @Value.Auxiliary
  public double getBlue() {
    return getZ();
  }

  public Vector3 add(Vector3 vector) {
    double x = getX() + vector.getX();
    double y = getY() + vector.getY();
    double z = getZ() + vector.getZ();

    return Vector3.builder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

  public Vector3 subtract(Vector3 vector) {
    double x = getX() - vector.getX();
    double y = getY() - vector.getY();
    double z = getZ() - vector.getZ();

    return Vector3.builder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

  public Vector3 multiply(Vector3 vector) {
    double x = getX() * vector.getX();
    double y = getY() * vector.getY();
    double z = getZ() * vector.getZ();

    return Vector3.builder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

  public Vector3 divide(Vector3 vector) {
    double x = getX() / vector.getX();
    double y = getY() / vector.getY();
    double z = getZ() / vector.getZ();

    return Vector3.builder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

  public Vector3 scalarMultiply(double scalar) {
    double x = getX() * scalar;
    double y = getY() * scalar;
    double z = getZ() * scalar;

    return Vector3.builder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

  public Vector3 scalarDivide(double scalar) {
    double x = getX() / scalar;
    double y = getY() / scalar;
    double z = getZ() / scalar;

    return Vector3.builder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

  public double dotProduct(Vector3 vector) {
    double x = getX() * vector.getX();
    double y = getY() * vector.getY();
    double z = getZ() * vector.getZ();

    return x + y + z;
  }

  public Vector3 cross(Vector3 vector) {
    double x = (getY() * vector.getZ()) - (getZ() * vector.getY());
    double y = (getZ() * vector.getX()) - (getX() * vector.getZ());
    double z = (getX() * vector.getY()) - (getY() * vector.getX());

    return Vector3.builder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

  public Vector3 unit() {
    return scalarDivide(getNorm());
  }

  @JsonIgnore
  @Value.Lazy
  public double getSquaredLength() {
    return (getX() * getX()) + (getY() * getY()) + (getZ() * getZ());
  }

  @JsonIgnore
  @Value.Lazy
  public double getNorm() {
    double xSquared = getX() * getX();
    double ySquared = getY() * getY();
    double zSquared = getZ() * getZ();

    return Math.sqrt(xSquared + ySquared + zSquared);
  }

  public Vector3 negate() {
    double x = -getX();
    double y = -getY();
    double z = -getZ();

    return Vector3.builder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

  public Vector3 apply(Function<Double, Double> function) {
    double x = function.apply(getX());
    double y = function.apply(getY());
    double z = function.apply(getZ());

    return Vector3.builder()
        .setX(x)
        .setY(y)
        .setZ(z)
        .build();
  }

  public static Vector3 zero() {
    return ZERO;
  }

  public static Vector3 one() {
    return ONE;
  }

  public static Vector3 max() {
    return MAX;
  }

  public static Vector3 min() {
    return MIN;
  }

}
