package com.github.nhirakawa.hyperbeam.shape;

import java.util.List;

import com.github.nhirakawa.hyperbeam.AlgebraicSceneObject;
import com.github.nhirakawa.hyperbeam.AlgebraicSceneObjects;
import com.google.common.collect.ImmutableList;

public class SceneObjectsList implements SceneObject {

  private final List<AlgebraicSceneObject> hittables;

  public SceneObjectsList(List<AlgebraicSceneObject> hittables) {
    this.hittables = ImmutableList.copyOf(hittables);
  }

  public List<AlgebraicSceneObject> getHittables() {
    return hittables;
  }

  @Override
  public SceneObjectType getShapeType() {
    return SceneObjectType.SCENE_OBJECTS_LIST;
  }

  @Override
  public AlgebraicSceneObject toAlgebraicSceneObject() {
    return AlgebraicSceneObjects.SCENE_OBJECTS_LIST(this);
  }

}
