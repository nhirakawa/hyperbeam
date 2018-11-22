package com.github.nhirakawa.ray.tracing.material;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.github.nhirakawa.ray.tracing.collision.HitRecord;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "materialType")
@JsonSubTypes({
    @Type(value = DielectricMaterial.class, name = "DIELECTRIC"),
    @Type(value = MetalMaterial.class, name = "METAL"),
    @Type(value = LambertianMaterial.class, name = "LAMBERTIAN")
})
public abstract class Material {

  public abstract MaterialType getMaterialType();

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
