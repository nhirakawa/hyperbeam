package com.github.nhirakawa.ray.tracing.scene;

import java.util.List;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.camera.Camera;
import com.github.nhirakawa.ray.tracing.shape.Shape;

@Value.Immutable
@ImmutableStyle
public interface SceneModel {

  Camera getCamera();
  List<Shape> getShapes();

}
