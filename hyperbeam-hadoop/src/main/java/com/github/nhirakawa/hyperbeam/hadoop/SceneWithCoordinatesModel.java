package com.github.nhirakawa.hyperbeam.hadoop;

import org.immutables.value.Value;

import com.github.nhirakawa.hyperbeam.geometry.Coordinates;
import com.github.nhirakawa.hyperbeam.scene.Scene;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface SceneWithCoordinatesModel {

  Coordinates getCoordinates();
  Scene getScene();

}
