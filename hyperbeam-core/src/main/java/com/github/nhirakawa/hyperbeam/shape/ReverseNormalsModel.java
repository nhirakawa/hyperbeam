package com.github.nhirakawa.hyperbeam.shape;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.AlgebraicSceneObject;
import com.github.nhirakawa.hyperbeam.AlgebraicSceneObjects;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public abstract class ReverseNormalsModel implements SceneObject {

  public abstract AlgebraicSceneObject getSceneObject();

  @Override
  public SceneObjectType getShapeType() {
    return SceneObjectType.REVERSE_NORMALS;
  }

  @Override
  @Value.Lazy
  @JsonIgnore
  public AlgebraicSceneObject toAlgebraicSceneObject() {
    return AlgebraicSceneObjects.REVERSE_NORMALS(this);
  }

}
