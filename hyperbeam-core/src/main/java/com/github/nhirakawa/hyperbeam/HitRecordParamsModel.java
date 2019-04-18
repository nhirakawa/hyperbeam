package com.github.nhirakawa.hyperbeam;

import org.immutables.value.Value;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@ImmutableStyle
@Value.Immutable
public interface HitRecordParamsModel {

  Ray getRay();
  double getTMin();
  double getTMax();

}
