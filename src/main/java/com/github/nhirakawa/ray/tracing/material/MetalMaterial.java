package com.github.nhirakawa.ray.tracing.material;

import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.shape.HitRecord;
import com.github.nhirakawa.ray.tracing.util.VectorUtils;

public class MetalMaterial extends Material {

  private final Vector3 albedo;
  private final double fuzz;

  public MetalMaterial(Vector3 albedo, double fuzz) {
    this.albedo = albedo;
    this.fuzz = Math.min(fuzz, 1);
  }

  @Override
  public MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord) {
    Vector3 reflected = reflect(inRay.getDirection().unit(), hitRecord.getNormal());
    Ray scatteredRay = Ray.builder()
        .setOrigin(hitRecord.getPoint())
        .setDirection(reflected.add(VectorUtils.getRandomUnitSphereVector().scalarMultiply(fuzz)))
        .setTime(inRay.getTime())
        .build();
    boolean wasScattered = scatteredRay.getDirection().dotProduct(hitRecord.getNormal()) > 0;

    return MaterialScatterRecord.builder()
        .setAttenuation(albedo)
        .setScattered(scatteredRay)
        .setWasScattered(wasScattered)
        .build();
  }

}
