package com.github.nhirakawa.ray.tracing.camera;

import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;

public class Camera {

  private static final Vector3 LOWER_LEFT_CORNER = new Vector3(-2, -1, -1);
  private static final Vector3 HORIZONTAL = new Vector3(4, 0, 0);
  private static final Vector3 VERTICAL = new Vector3(0, 2, 0);
  private static final Vector3 ORIGIN = new Vector3(0, 0, 0);

  public Ray getRay(double u, double v) {
    return new Ray(ORIGIN, LOWER_LEFT_CORNER.add(HORIZONTAL.scalarMultiply(u)).add(VERTICAL.scalarMultiply(v)));
  }

}
