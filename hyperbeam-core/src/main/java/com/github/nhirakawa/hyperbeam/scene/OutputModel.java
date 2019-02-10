package com.github.nhirakawa.hyperbeam.scene;

import java.awt.image.BufferedImage;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface OutputModel {
  
  int getNumberOfColumns();
  int getNumberOfRows();
  int getNumberOfSamples();

  @Value.Lazy
  @JsonIgnore
  default BufferedImage getEmptyBufferedImage() {
    return new BufferedImage(getNumberOfRows(), getNumberOfColumns(), BufferedImage.TYPE_3BYTE_BGR);
  }

}
