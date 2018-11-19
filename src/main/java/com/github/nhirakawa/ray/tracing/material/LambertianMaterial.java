package com.github.nhirakawa.ray.tracing.material;

import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.shape.HitRecord;
import com.github.nhirakawa.ray.tracing.util.VectorUtils;

public class LambertianMaterial extends Material {

  private final Vector3 albedo;

  public LambertianMaterial(Vector3 albedo) {
    this.albedo = albedo;
  }

  @Override
  public MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord) {
    Vector3 target = hitRecord.getPoint().add(hitRecord.getNormal()).add(VectorUtils.getRandomUnitSphereVector());
    Ray scatteredRay = Ray.builder()
        .setOrigin(hitRecord.getPoint())
        .setDirection(target.subtract(hitRecord.getPoint()))
        .setTime(inRay.getTime())
        .build();

    return MaterialScatterRecord.builder()
        .setAttenuation(albedo)
        .setScattered(scatteredRay)
        .setWasScattered(true)
        .build();
  }
}
