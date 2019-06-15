package com.github.nhirakawa.hyperbeam.shape;

import java.util.List;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public abstract class BoundingVolumeHierarchyModel implements SceneObject {

  public abstract List<SceneObject> getSortedSceneObjects();
  public abstract double getTime0();
  public abstract double getTime1();

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.BOUNDING_VOLUME_HIERARCHY;
  }

  @Value.Lazy
  @JsonIgnore
  public SceneObject getLeft() {
    List<SceneObject> sortedHittablesList = getSortedSceneObjects();
    int size = sortedHittablesList.size();

    if (size == 1) {
      return sortedHittablesList.get(0);
    } else if (size == 2) {
      return sortedHittablesList.get(0);
    } else {
      return BoundingVolumeHierarchy.builder()
          .setSortedSceneObjects(sortedHittablesList.subList(0, size / 2))
          .setTime0(getTime0())
          .setTime1(getTime1())
          .build();
    }
  }

  @Value.Lazy
  @JsonIgnore
  public SceneObject getRight() {
    List<SceneObject> sortedHittablesList = getSortedSceneObjects();
    int size = sortedHittablesList.size();

    if (size == 1) {
      return sortedHittablesList.get(0);
    } else if (size == 2) {
      return sortedHittablesList.get(1);
    } else {
      return BoundingVolumeHierarchy.builder()
          .setSortedSceneObjects(sortedHittablesList.subList(size / 2, size))
          .setTime0(getTime0())
          .setTime1(getTime1())
          .build();
    }
  }

}
