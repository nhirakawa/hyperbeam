package com.github.nhirakawa.hyperbeam.material;

import org.immutables.value.Value;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.texture.Texture;
import com.github.nhirakawa.hyperbeam.util.VectorUtils;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public abstract class LambertianMaterialModel extends Material {

  public abstract Texture getTexture();

  @Override
  @Value.Auxiliary
  public MaterialType getMaterialType() {
    return MaterialType.LAMBERTIAN;
  }

  @Override
  public MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord) {
    Vector3 random = VectorUtils.getRandomUnitSphereVector();

    Vector3 target = Vector3.builder()
        .setX(hitRecord.getPoint().getX() + hitRecord.getNormal().getX() + random.getX())
        .setY(hitRecord.getPoint().getY() + hitRecord.getNormal().getY() + random.getY())
        .setZ(hitRecord.getPoint().getZ() + hitRecord.getNormal().getZ() + random.getZ())
        .build();


    Ray scatteredRay = Ray
      .builder()
      .setOrigin(hitRecord.getPoint())
      .setDirection(target.subtract(hitRecord.getPoint()))
      .setTime(inRay.getTime())
      .build();

    return MaterialScatterRecord
      .builder()
      .setAttenuation(getTexture().getValue(0, 0, hitRecord.getPoint()))
      .setScattered(scatteredRay)
      .setWasScattered(true)
      .build();
  }
}
