package com.github.nhirakawa.ray.tracing.material;

import java.util.Optional;

import com.github.nhirakawa.ray.tracing.collision.HitRecord;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;


public abstract class Material {

  public abstract MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord);

  protected static Optional<Vector3> refract(Vector3 vector, Vector3 normal, double niOverNt) {
    Vector3 unitVector = vector.unit();
    double dt = unitVector.dotProduct(normal);
    double discriminant = 1 - (niOverNt * niOverNt * (1 - (dt * dt)));
    if (discriminant > 0) {
      Vector3 firstPart = unitVector.subtract(normal.scalarMultiply(dt)).scalarMultiply(niOverNt);
      Vector3 secondPart = normal.scalarMultiply(Math.sqrt(discriminant));

      return Optional.of(firstPart.subtract(secondPart));
    } else {
      return Optional.empty();
    }
  }

  protected static Vector3 reflect(Vector3 vector, Vector3 normal) {
    return vector.subtract(normal.scalarMultiply(2).scalarMultiply(vector.dotProduct(normal)));
  }
}
