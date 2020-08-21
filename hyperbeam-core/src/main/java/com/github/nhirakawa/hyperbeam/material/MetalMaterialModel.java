package com.github.nhirakawa.hyperbeam.material;

import org.immutables.value.Value;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.util.VectorUtils;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

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
    Vector3 reflected = reflect(
      inRay.getDirection().unit(),
      hitRecord.getNormal()
    );

    Vector3 random = VectorUtils.getRandomUnitSphereVector();

    Vector3 direction = Vector3.builder()
        .setX(reflected.getX() + (random.getX() * getFuzz()))
        .setY(reflected.getY() + (random.getY() + getFuzz()))
        .setZ(reflected.getZ() + (random.getZ() + getFuzz()))
        .build();

    Ray scatteredRay = Ray
      .builder()
      .setOrigin(hitRecord.getPoint())
      .setDirection(
          direction
      )
      .setTime(inRay.getTime())
      .build();
    boolean wasScattered = scatteredRay
      .getDirection()
      .dotProduct(hitRecord.getNormal()) >
      0;

    return MaterialScatterRecord
      .builder()
      .setAttenuation(getAlbedo())
      .setScattered(scatteredRay)
      .setWasScattered(wasScattered)
      .build();
  }
}
