package com.github.nhirakawa.hyperbeam.texture;

import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public abstract class ConstantTextureModel implements Texture {

  public abstract Vector3 getColor();

  @Override
  @Value.Auxiliary
  public TextureType getTextureType() {
    return TextureType.CONSTANT;
  }

  @Override
  public Vector3 getValue(double u, double v, Vector3 point) {
    return getColor();
  }
}
