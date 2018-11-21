package com.github.nhirakawa.ray.tracing.texture;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;

@Value.Immutable
@ImmutableStyle
public abstract class CheckerTextureModel implements Texture {

  public abstract Texture getTexture0();
  public abstract Texture getTexture1();

  @Override
  public Vector3 getValue(double u, double v, Vector3 point) {
    double sines = StrictMath.sin(10 * point.getX()) * StrictMath.sin(10 * point.getY()) * StrictMath.sin(10 * point.getZ());
    if (sines < 0) {
      return getTexture1().getValue(u, v, point);
    } else {
      return getTexture0().getValue(u, v, point);
    }
  }

}
