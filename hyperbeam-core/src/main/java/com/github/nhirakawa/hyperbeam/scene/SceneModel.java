package com.github.nhirakawa.hyperbeam.scene;

import com.github.nhirakawa.hyperbeam.camera.Camera;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface SceneModel {
  Camera getCamera();
  List<SceneObject> getSceneObjects();
}
