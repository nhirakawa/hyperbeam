package com.github.nhirakawa.hyperbeam.shape;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public abstract class ReverseNormalsModel implements SceneObject {

  public abstract SceneObject getSceneObject();

  @Override
  public SceneObjectType getShapeType() {
    return SceneObjectType.REVERSE_NORMALS;
  }
}
