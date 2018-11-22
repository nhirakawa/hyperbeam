package com.github.nhirakawa.ray.tracing.material;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.collision.HitRecord;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.util.VectorUtils;

@Value.Immutable
@ImmutableStyle
public abstract class MetalMaterialModel extends Material {

  public abstract Vector3 getAlbedo();
  public abstract double getFuzz();

  @Override
  @Value.Auxiliary
  public MaterialType getMaterialType() {
    return MaterialType.METAL;
  }

  @Override
  public MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord) {
    Vector3 reflected = reflect(inRay.getDirection().unit(), hitRecord.getNormal());
    Ray scatteredRay = Ray.builder()
        .setOrigin(hitRecord.getPoint())
        .setDirection(reflected.add(VectorUtils.getRandomUnitSphereVector().scalarMultiply(getFuzz())))
        .setTime(inRay.getTime())
        .build();
    boolean wasScattered = scatteredRay.getDirection().dotProduct(hitRecord.getNormal()) > 0;

    return MaterialScatterRecord.builder()
        .setAttenuation(getAlbedo())
        .setScattered(scatteredRay)
        .setWasScattered(wasScattered)
        .build();
  }

}
