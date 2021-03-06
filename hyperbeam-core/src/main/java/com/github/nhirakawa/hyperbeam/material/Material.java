package com.github.nhirakawa.hyperbeam.material;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import java.util.Optional;

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
      Vector3 firstPart = unitVector
        .subtract(normal.scalarMultiply(dt))
        .scalarMultiply(niOverNt);
      Vector3 secondPart = normal.scalarMultiply(Math.sqrt(discriminant));

      return Optional.of(firstPart.subtract(secondPart));
    } else {
      return Optional.empty();
    }
  }

  protected static Vector3 reflect(Vector3 vector, Vector3 normal) {
    return vector.subtract(
      normal.scalarMultiply(2).scalarMultiply(vector.dotProduct(normal))
    );
  }
}
