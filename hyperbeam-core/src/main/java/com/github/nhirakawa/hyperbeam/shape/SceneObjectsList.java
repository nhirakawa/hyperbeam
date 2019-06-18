package com.github.nhirakawa.hyperbeam.shape;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public class SceneObjectsList implements SceneObject {

  private static final Logger LOG = LoggerFactory.getLogger(SceneObjectsList.class);

  private final List<? extends SceneObject> hittables;

  public SceneObjectsList(List<? extends SceneObject> hittables) {
    this.hittables = ImmutableList.copyOf(hittables);
  }

  public List<? extends SceneObject> getHittables() {
    return hittables;
  }

  @Override
  public SceneObjectType getShapeType() {
    return SceneObjectType.SCENE_OBJECTS_LIST;
  }

}
