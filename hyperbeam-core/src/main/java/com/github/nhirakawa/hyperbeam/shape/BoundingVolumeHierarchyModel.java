package com.github.nhirakawa.hyperbeam.shape;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.RayProcessor;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import java.util.List;
import java.util.Optional;
import org.immutables.value.Value;

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
      return BoundingVolumeHierarchy
        .builder()
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
      return BoundingVolumeHierarchy
        .builder()
        .setSortedSceneObjects(sortedHittablesList.subList(size / 2, size))
        .setTime0(getTime0())
        .setTime1(getTime1())
        .build();
    }
  }

  @Override
  public Optional<HitRecord> hit(
    RayProcessor rayProcessor,
    Ray ray,
    double tMin,
    double tMax
  ) {
    return rayProcessor.hitBoundingVolumeHierarchy(this, ray, tMin, tMax);
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(
    RayProcessor rayProcessor,
    double t0,
    double t1
  ) {
    return rayProcessor.getBoundingBoxForBoundingVolumeHierarcy(this, t0, t1);
  }
}
