package com.github.nhirakawa.hyperbeam.material;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;

@SuppressWarnings({ "ClassReferencesSubclass", "HardCodedStringLiteral" })
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "materialType"
)
@JsonSubTypes(
  {
    @Type(value = DielectricMaterial.class, name = "DIELECTRIC"),
    @Type(value = MetalMaterial.class, name = "METAL"),
    @Type(value = LambertianMaterial.class, name = "LAMBERTIAN"),
    @Type(value = DiffuseLightMaterial.class, name = "DIFFUSE_LIGHT"),
    @Type(value = IsotropicMaterial.class, name = "ISOTROPIC")
  }
)
public abstract class Material {

  @SuppressWarnings("unused")
  public abstract MaterialType getMaterialType();

  public abstract MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord);

  public Vector3 emit(double u, double v, Vector3 point) {
    return Vector3.zero();
  }

  protected static Optional<Vector3> refract(
    Vector3 vector,
    Vector3 normal,
    double niOverNt
  ) {
    Vector3 unitVector = vector.unit();
    double dt = unitVector.dotProduct(normal);
    double discriminant = 1 - (niOverNt * niOverNt * (1 - (dt * dt)));
    if (discriminant > 0) {
      double firstPartX = unitVector.getX() - ((normal.getX() * dt) * niOverNt);
      double firstPartY = (unitVector.getY() - (normal.getY() * dt)) * niOverNt;
      double firstPartZ = (unitVector.getZ() - (normal.getZ() * dt)) * niOverNt;

      double secondPartX = normal.getX() * Math.sqrt(discriminant);
      double secondPartY = normal.getY() * Math.sqrt(discriminant);
      double secondPartZ = normal.getZ() * Math.sqrt(discriminant);

      return Optional.of(
          Vector3.builder()
          .setX(firstPartX - secondPartX)
          .setY(firstPartY - secondPartY)
          .setZ(firstPartZ - secondPartZ)
          .build()
      );
    } else {
      return Optional.empty();
    }
  }

  protected static Vector3 reflect(Vector3 vector, Vector3 normal) {
    double dot = vector.dotProduct(normal);

    return Vector3.builder()
        .setX(vector.getX() - 2 * normal.getX() * dot)
        .setY(vector.getY() - 2 * normal.getY() * dot)
        .setZ(vector.getZ() - 2 * normal.getZ() * dot)
        .build();
  }
}
