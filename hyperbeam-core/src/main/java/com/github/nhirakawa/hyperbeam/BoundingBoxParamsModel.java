package com.github.nhirakawa.hyperbeam;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;

@ImmutableStyle
@Value.Immutable
public interface BoundingBoxParamsModel {

  double getT0();
  double getT1();

}
