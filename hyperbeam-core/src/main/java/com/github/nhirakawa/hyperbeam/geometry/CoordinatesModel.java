package com.github.nhirakawa.hyperbeam.geometry;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface CoordinatesModel {
  int getX();
  int getY();
}
