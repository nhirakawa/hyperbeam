package com.github.nhirakawa.ray.tracing.texture;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.noise.PerlinNoise;

@Value.Immutable
@ImmutableStyle
public abstract class PerlinNoiseTextureModel implements Texture {

  private static final Vector3 ONE_VECTOR = new Vector3(1, 1, 1);

  public abstract double getScale();

  @Override
  public Vector3 getValue(double u, double v, Vector3 point) {
    return ONE_VECTOR.scalarMultiply(0.5).scalarMultiply(1 + Math.sin(getScale() * point.getZ() + 10 * PerlinNoise.turbulence(point)));
  }

  @Override
  public TextureType getTextureType() {
    return TextureType.PERLIN_NOISE;
  }
}
