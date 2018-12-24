package com.github.nhirakawa.hyperbeam.color;

import java.awt.*;

import org.immutables.value.Value;

import com.github.nhirakawa.hyperbeam.geometry.Coordinates;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

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
