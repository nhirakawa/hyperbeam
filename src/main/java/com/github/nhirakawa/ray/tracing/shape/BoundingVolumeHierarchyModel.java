package com.github.nhirakawa.ray.tracing.shape;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.util.MathUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

@Value.Immutable
@ImmutableStyle
public abstract class BoundingVolumeHierarchyModel implements SceneObject {

  private static final Function<AxisAlignedBoundingBox, Double> MIN_X = aabb -> aabb.getMin().getX();
  private static final Function<AxisAlignedBoundingBox, Double> MIN_Y = aabb -> aabb.getMin().getY();
  private static final Function<AxisAlignedBoundingBox, Double> MIN_Z = aabb -> aabb.getMin().getZ();

  public abstract SceneObjectsList getSceneObjectsList();
  public abstract double getTime0();
  public abstract double getTime1();

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.BOUNDING_VOLUME_HIERARCHY;
  }

  @Value.Lazy
  @JsonIgnore
  public List<SceneObject> getSortedHittablesList() {
    final Comparator<SceneObject> comparator;
    switch ((int) (MathUtils.rand() * 3)) {
      case 0:
        comparator = getComparator(MIN_X);
        break;
      case 1:
        comparator = getComparator(MIN_Y);
        break;
      case 2:
        comparator = getComparator(MIN_Z);
        break;
      default:
        throw new IllegalArgumentException("Invalid random number when sorting hittables");
    }

    return getSceneObjectsList().getHittables().stream()
        .sorted(comparator)
        .collect(ImmutableList.toImmutableList());
  }

  @Value.Lazy
  @JsonIgnore
  public SceneObject getLeft() {
    List<SceneObject> sortedHittablesList = getSortedHittablesList();
    int size = sortedHittablesList.size();

    if (size == 1) {
      return sortedHittablesList.get(0);
    } else if (size == 2) {
      return sortedHittablesList.get(0);
    } else {
      return BoundingVolumeHierarchy.builder()
          .setSceneObjectsList(new SceneObjectsList(sortedHittablesList.subList(0, size / 2)))
          .setTime0(getTime0())
          .setTime1(getTime1())
          .build();
    }
  }

  @Value.Lazy
  @JsonIgnore
  public SceneObject getRight() {
    List<SceneObject> sortedHittablesList = getSortedHittablesList();
    int size = sortedHittablesList.size();

    if (size == 1) {
      return sortedHittablesList.get(0);
    } else if (size == 2) {
      return sortedHittablesList.get(1);
    } else {
      return BoundingVolumeHierarchy.builder()
          .setSceneObjectsList(new SceneObjectsList(sortedHittablesList.subList(size / 2, size)))
          .setTime0(getTime0())
          .setTime1(getTime1())
          .build();
    }
  }

  @Value.Lazy
  @JsonIgnore
  public AxisAlignedBoundingBox getAxisAlignedBoundingBox() {
    Optional<AxisAlignedBoundingBox> leftBox = getLeft().getBoundingBox(getTime0(), getTime1());
    Optional<AxisAlignedBoundingBox> rightBox = getRight().getBoundingBox(getTime0(), getTime1());

    Preconditions.checkState(leftBox.isPresent(), "Could not get bounding box for %s", getLeft());
    Preconditions.checkState(rightBox.isPresent(), "Could not get bounding box for %s", getRight());

    return AxisAlignedBoundingBox.getSurroundingBox(leftBox.get(), rightBox.get());
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    if (getAxisAlignedBoundingBox().hit(ray, tMin, tMax)) {
      Optional<HitRecord> leftHitRecord = getLeft().hit(ray, tMin, tMax);
      Optional<HitRecord> rightHitRecord = getRight().hit(ray, tMin, tMax);

      if (leftHitRecord.isPresent() && rightHitRecord.isPresent()) {
        if (leftHitRecord.get().getT() < rightHitRecord.get().getT()) {
          return leftHitRecord;
        } else {
          return rightHitRecord;
        }
      } else if (leftHitRecord.isPresent()) {
        return leftHitRecord;
      } else if (rightHitRecord.isPresent()) {
        return rightHitRecord;
      } else {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return Optional.of(getAxisAlignedBoundingBox());
  }

  private static Comparator<SceneObject> getComparator(Function<AxisAlignedBoundingBox, Double> function) {
    return (hittable1, hittable2) -> {
      Optional<AxisAlignedBoundingBox> box1 = hittable1.getBoundingBox(0, 0);
      Optional<AxisAlignedBoundingBox> box2 = hittable2.getBoundingBox(0, 0);

      if (function.apply(box1.get()) - function.apply(box2.get()) < 0) {
        return -1;
      } else {
        return 1;
      }
    };
  }

}
