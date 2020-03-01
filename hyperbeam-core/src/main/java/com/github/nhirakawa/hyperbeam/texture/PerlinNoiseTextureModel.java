package com.github.nhirakawa.hyperbeam.texture;

import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.noise.PerlinNoise;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public abstract class PerlinNoiseTextureModel implements Texture {

  public abstract double getScale();

  @Override
  public Vector3 getValue(double u, double v, Vector3 point) {
    return Vector3
      .one()
      .scalarMultiply(0.5)
      .scalarMultiply(
        1 +
          Math.sin(
            getScale() * point.getZ() + 10 * PerlinNoise.turbulence(point)
          )
      );
  }

  @Override
  public TextureType getTextureType() {
    return TextureType.PERLIN_NOISE;
  }
}
