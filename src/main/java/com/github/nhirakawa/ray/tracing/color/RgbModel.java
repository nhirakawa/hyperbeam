package com.github.nhirakawa.ray.tracing.color;

import java.awt.*;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.geometry.Coordinates;

@Value.Immutable
@ImmutableStyle
public interface RgbModel {

  Coordinates getCoordinates();
  int getRed();
  int getGreen();
  int getBlue();

  @Value.Derived
  default Color getColor() {
    return new Color(getRed(), getGreen(), getBlue());
  }

}
