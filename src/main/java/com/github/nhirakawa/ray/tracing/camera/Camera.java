package com.github.nhirakawa.ray.tracing.camera;

import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.util.VectorUtils;

public class Camera {

  private final double lensRadius;
  private final Vector3 u;
  private final Vector3 v;
  private final Vector3 w;
  private final Vector3 origin;
  private final Vector3 lowerLeftCorner;
  private final Vector3 horizontal;
  private final Vector3 vertical;

  public Camera(Vector3 lookFrom,
                Vector3 lookAt,
                Vector3 viewUp,
                double verticalFovDegrees,
                double aspect,
                double aperture,
                double focusDistance) {
    double theta = (verticalFovDegrees * Math.PI) / 180;
    double halfHeight = StrictMath.tan(theta / 2);
    double halfWidth = aspect * halfHeight;

    this.w = lookFrom.subtract(lookAt).unit();
    this.u = viewUp.cross(w).unit();
    this.v = w.cross(u);

    this.lensRadius = aperture / 2;
    this.origin = lookFrom;
    this.lowerLeftCorner = origin.subtract(u.scalarMultiply(halfWidth * focusDistance))
        .subtract(v.scalarMultiply(halfHeight * focusDistance))
        .subtract(w.scalarMultiply(focusDistance));
    this.horizontal = u.scalarMultiply(2 * focusDistance * halfWidth);
    this.vertical = v.scalarMultiply(2 * focusDistance * halfHeight);
  }

  public Ray getRay(double s, double t) {
    Vector3 rd = VectorUtils.getRandomVectorInUnitDisk().scalarMultiply(lensRadius);
    Vector3 offset = u.scalarMultiply(rd.getX()).add(v.scalarMultiply(rd.getY()));

    return new Ray(
        origin.add(offset),
        lowerLeftCorner.add(horizontal.scalarMultiply(s))
            .add(vertical.scalarMultiply(t))
            .subtract(origin)
            .subtract(offset)
    );
  }

}
