package com.github.nhirakawa.ray.tracing.material;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.collision.HitRecord;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.texture.Texture;

@Value.Immutable
@ImmutableStyle
public abstract class DiffuseLightMaterialModel extends Material {

  public abstract Texture getTexture();

  @Override
  public MaterialType getMaterialType() {
    return MaterialType.DIFFUSE_LIGHT;
  }

  @Override
  public MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord) {
    return MaterialScatterRecord.builder()
        .setAttenuation(Vector3.zero())
        .setScattered(
            Ray.builder()
                .setDirection(Vector3.zero())
                .setOrigin(Vector3.zero())
                .setTime(0)
                .build()
        )
        .setWasScattered(false)
        .build();
  }

  @Override
  public Vector3 emit(double u, double v, Vector3 point) {
    return getTexture().getValue(u, v, point);
  }

}
