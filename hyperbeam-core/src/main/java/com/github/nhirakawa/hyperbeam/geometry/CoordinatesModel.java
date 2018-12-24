package com.github.nhirakawa.hyperbeam.geometry;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface CoordinatesModel {

  int getX();
  int getY();

}