package com.github.nhirakawa.ray.tracing.camera;

import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;

public class Camera {

  private final Vector3 origin;
  private final Vector3 lowerLeftCorner;
  private final Vector3 horizontal;
  private final Vector3 vertical;

  public Camera(Vector3 lookFrom, Vector3 lookAt, Vector3 viewUp, double verticalFovDegrees, double aspect) {
    double theta = (verticalFovDegrees * Math.PI) / 180;
    double halfHeight = StrictMath.tan(theta / 2);
    double halfWidth = aspect * halfHeight;

    Vector3 w = lookFrom.subtract(lookAt).unit();
    Vector3 u = viewUp.cross(w).unit();
    Vector3 v = w.cross(u);

    this.origin = lookFrom;
    this.lowerLeftCorner = origin.subtract(u.scalarMultiply(halfWidth)).subtract(v.scalarMultiply(halfHeight)).subtract(w);
    this.horizontal = u.scalarMultiply(2 * halfWidth);
    this.vertical = v.scalarMultiply(2 * halfHeight);
  }

  public Ray getRay(double u, double v) {
    return new Ray(origin, lowerLeftCorner.add(horizontal.scalarMultiply(u)).add(vertical.scalarMultiply(v)).subtract(origin));
  }

}
