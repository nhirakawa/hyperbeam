package com.github.nhirakawa.ray.tracing.camera;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.util.MathUtils;
import com.github.nhirakawa.ray.tracing.util.VectorUtils;

@Value.Immutable
@ImmutableStyle
public interface CameraModel {

  Vector3 getLookFrom();
  Vector3 getLookAt();
  Vector3 getViewUp();
  double getVerticalFovDegrees();
  double getAspectRatio();
  double getAperture();
  double getFocusDistance();
  double getTime0();
  double getTime1();

  @Value.Lazy
  @JsonIgnore
  default double getTheta() {
    return (getVerticalFovDegrees() * Math.PI) / 180;
  }

  @Value.Lazy
  @JsonIgnore
  default double getHalfHeight() {
    return StrictMath.tan(getTheta() / 2);
  }

  @Value.Lazy
  @JsonIgnore
  default double getHalfWidth() {
    return getAspectRatio() * getHalfHeight();
  }

  @Value.Lazy
  @JsonIgnore
  default Vector3 getW() {
    return getLookFrom().subtract(getLookAt()).unit();
  }

  @Value.Lazy
  @JsonIgnore
  default Vector3 getU() {
    return getViewUp().cross(getW()).unit();
  }

  @Value.Lazy
  @JsonIgnore
  default Vector3 getV() {
    return getW().cross(getU());
  }

  @Value.Lazy
  @JsonIgnore
  default double getLensRadius() {
    return getAperture() / 2;
  }

  @Value.Lazy
  @JsonIgnore
  default Vector3 getOrigin() {
    return getLookFrom();
  }

  @Value.Lazy
  @JsonIgnore
  default Vector3 getLowerLeftCorner() {
    return getOrigin().subtract(getU().scalarMultiply(getHalfWidth() * getFocusDistance()))
        .subtract(getV().scalarMultiply(getHalfHeight() * getFocusDistance()))
        .subtract(getW().scalarMultiply(getFocusDistance()));
  }

  @Value.Lazy
  @JsonIgnore
  default Vector3 getHorizontal() {
    return getU().scalarMultiply(2 * getFocusDistance() * getHalfWidth());
  }

  @Value.Lazy
  @JsonIgnore
  default Vector3 getVertical() {
    return getV().scalarMultiply(2 * getFocusDistance() * getHalfHeight());
  }

  default Ray getRay(double s, double t) {
    Vector3 rd = VectorUtils.getRandomVectorInUnitDisk().scalarMultiply(getLensRadius());
    Vector3 offset = getU().scalarMultiply(rd.getX()).add(getV().scalarMultiply(rd.getY()));

    double time = getTime0() + (MathUtils.rand() * (getTime1() - getTime0()));

    return Ray.builder()
        .setOrigin(getOrigin().add(offset))
        .setDirection(
            getLowerLeftCorner().add(getHorizontal().scalarMultiply(s))
                .add(getVertical().scalarMultiply(t))
                .subtract(getOrigin())
                .subtract(offset)
        )
        .setTime(time)
        .build();
  }

}
