package com.github.nhirakawa.hyperbeam.scene;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface OutputModel {
  
  int getNumberOfColumns();
  int getNumberOfRows();
  int getNumberOfSamples();

}
