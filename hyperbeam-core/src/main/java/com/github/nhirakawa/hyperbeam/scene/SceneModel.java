package com.github.nhirakawa.hyperbeam.scene;

import java.util.List;

import org.immutables.value.Value;

import com.github.nhirakawa.hyperbeam.camera.Camera;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdt;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface SceneModel {

  Camera getCamera();
  List<ShapeAdt> getSceneObjects();

}
