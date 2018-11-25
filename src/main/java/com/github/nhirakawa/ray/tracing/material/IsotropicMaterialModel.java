package com.github.nhirakawa.ray.tracing.material;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.collision.HitRecord;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.texture.Texture;
import com.github.nhirakawa.ray.tracing.util.VectorUtils;

@Value.Immutable
@ImmutableStyle
public abstract class IsotropicMaterialModel extends Material {

  public abstract Texture getTexture();

  @Override
  public MaterialType getMaterialType() {
    return MaterialType.ISOTROPIC;
  }

  @Override
  public MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord) {
    return MaterialScatterRecord.builder()
        .setScattered(
            Ray.builder()
                .setOrigin(hitRecord.getPoint())
                .setDirection(VectorUtils.getRandomUnitSphereVector())
                .setTime(0)
                .build()
        )
        .setAttenuation(getTexture().getValue(hitRecord.getU(), hitRecord.getV(), hitRecord.getPoint()))
        .setWasScattered(true)
        .build();
  }

}
