package com.github.nhirakawa.hyperbeam.scene;

import java.util.List;

import org.immutables.value.Value;

import com.github.nhirakawa.hyperbeam.camera.Camera;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public interface SerializedSceneModel {

  Camera getCamera();
  List<SceneObject> getSceneObjects();

}
