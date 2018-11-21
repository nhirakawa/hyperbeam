package com.github.nhirakawa.ray.tracing.material;

import static com.github.nhirakawa.ray.tracing.util.MathUtils.rand;

import java.util.Optional;

import com.github.nhirakawa.ray.tracing.collision.HitRecord;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;


public class DielectricMaterial extends Material {

  private static final Vector3 ATTENUATION = new Vector3(1, 1, 1);

  private final double refractiveIndex;

  public DielectricMaterial(double refractiveIndex) {
    this.refractiveIndex = refractiveIndex;
  }

  @Override
  public MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord) {
    Vector3 reflected = reflect(inRay.getDirection(), hitRecord.getNormal());

    final Vector3 outwardNormal;
    final double niOverNt;
    final double cosine;

    if (inRay.getDirection().dotProduct(hitRecord.getNormal()) > 0) {
      outwardNormal = hitRecord.getNormal().scalarMultiply(-1);
      niOverNt = refractiveIndex;
      cosine = (refractiveIndex * inRay.getDirection().dotProduct(hitRecord.getNormal())) / inRay.getDirection()
          .getNorm();
    } else {
      outwardNormal = hitRecord.getNormal();
      niOverNt = 1 / refractiveIndex;
      cosine = -inRay.getDirection().dotProduct(hitRecord.getNormal()) / inRay.getDirection().getNorm();
    }

    Optional<Vector3> refracted = refract(inRay.getDirection(), outwardNormal, niOverNt);

    final double reflectProbability;
    if (refracted.isPresent()) {
      reflectProbability = computeSchlick(cosine, refractiveIndex);
    } else {
      reflectProbability = 1;
    }

    if (rand() < reflectProbability) {
      return MaterialScatterRecord.builder()
          .setAttenuation(ATTENUATION)
          .setScattered(
              Ray.builder()
                  .setOrigin(hitRecord.getPoint())
                  .setDirection(reflected)
                  .setTime(inRay.getTime())
                  .build()
          )
          .setWasScattered(true)
          .build();
    } else {
      return MaterialScatterRecord.builder()
          .setAttenuation(ATTENUATION)
          .setScattered(
              Ray.builder()
                  .setOrigin(hitRecord.getPoint())
                  .setDirection(refracted.get())
                  .setTime(inRay.getTime())
                  .build()
          )
          .setWasScattered(true)
          .build();
    }
  }

  private static double computeSchlick(double cosine, double refractiveIndex) {
    double r0 = (1 - refractiveIndex) / (1 + refractiveIndex);
    double r0Squared = StrictMath.pow(r0, 2);
    return r0Squared + ((1 - r0Squared) * StrictMath.pow((1 - cosine), 5));
  }

}
