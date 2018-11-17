package com.github.nhirakawa.ray.tracing.material;

import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.shape.HitRecord;
import com.github.nhirakawa.ray.tracing.util.VectorUtils;

public class MetalMaterial implements Material {

  private final Vector3 albedo;
  private final double fuzz;

  public MetalMaterial(Vector3 albedo, double fuzz) {
    this.albedo = albedo;
    this.fuzz = Math.min(fuzz, 1);
  }

  @Override
  public MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord) {
    Vector3 reflected = reflect(inRay.getDirection().unit(), hitRecord.getNormal());
    Ray scatteredRay = new Ray(hitRecord.getPoint(), reflected.add(VectorUtils.getRandomUnitSphereVector().scalarMultiply(fuzz)));
    boolean wasScattered = scatteredRay.getDirection().dotProduct(hitRecord.getNormal()) > 0;
    return new MaterialScatterRecord(albedo, scatteredRay, wasScattered);
  }

  private static Vector3 reflect(Vector3 vector, Vector3 normal) {
    return vector.subtract(normal.scalarMultiply(2).scalarMultiply(vector.dotProduct(normal)));
  }
}
