package com.github.nhirakawa.hyperbeam.shape;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.util.MathUtils;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
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

  @Value.Derived
  @JsonIgnore
  public List<ShapeAdt> getSortedHittablesList() {
    final Comparator<ShapeAdt> comparator;
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

    return getSceneObjectsList().getShapeAdts().stream()
        .sorted(comparator)
        .collect(ImmutableList.toImmutableList());
  }

  @Value.Derived
  @JsonIgnore
  public ShapeAdt getLeft() {
    List<ShapeAdt> sortedHittablesList = getSortedHittablesList();
    int size = sortedHittablesList.size();

    if (size == 1) {
      return sortedHittablesList.get(0);
    } else if (size == 2) {
      return sortedHittablesList.get(0);
    } else {

      return ShapeAdts.BOUNDING_VOLUME_HIERARCHY(
          BoundingVolumeHierarchy.builder()
              .setSceneObjectsList(new SceneObjectsList(sortedHittablesList.subList(0, size / 2)))
              .setTime0(getTime0())
              .setTime1(getTime1())
              .build()
      );
    }
  }

  @Value.Derived
  @JsonIgnore
  public ShapeAdt getRight() {
    List<ShapeAdt> sortedHittablesList = getSortedHittablesList();
    int size = sortedHittablesList.size();

    if (size == 1) {
      return sortedHittablesList.get(0);
    } else if (size == 2) {
      return sortedHittablesList.get(1);
    } else {
      return ShapeAdts.BOUNDING_VOLUME_HIERARCHY(
          BoundingVolumeHierarchy.builder()
              .setSceneObjectsList(new SceneObjectsList(sortedHittablesList.subList(size / 2, size)))
              .setTime0(getTime0())
              .setTime1(getTime1())
              .build()
      );
    }
  }

  @Value.Derived
  @JsonIgnore
  public AxisAlignedBoundingBox getAxisAlignedBoundingBox() {
    Optional<AxisAlignedBoundingBox> leftBox = getBoundingBoxForShapeAdt(getLeft(), getTime0(), getTime1());
    Optional<AxisAlignedBoundingBox> rightBox = getBoundingBoxForShapeAdt(getRight(), getTime0(), getTime1());

    Preconditions.checkState(leftBox.isPresent(), "Could not get bounding box for %s", getLeft());
    Preconditions.checkState(rightBox.isPresent(), "Could not get bounding box for %s", getRight());

    return AxisAlignedBoundingBox.getSurroundingBox(leftBox.get(), rightBox.get());
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    if (getAxisAlignedBoundingBox().hit(ray, tMin, tMax)) {
      Optional<HitRecord> leftHitRecord = hitForShapeAdt(getLeft(), ray, tMin, tMax);
      Optional<HitRecord> rightHitRecord = hitForShapeAdt(getRight(), ray, tMin, tMax);

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

  private static Comparator<ShapeAdt> getComparator(Function<AxisAlignedBoundingBox, Double> function) {
    return (hittable1, hittable2) -> {
      Optional<AxisAlignedBoundingBox> box1 = getBoundingBoxForShapeAdt(hittable1, 0, 0);
      Optional<AxisAlignedBoundingBox> box2 = getBoundingBoxForShapeAdt(hittable2, 0, 0);

      if (function.apply(box1.get()) - function.apply(box2.get()) < 0) {
        return -1;
      } else {
        return 1;
      }
    };
  }

  private static Optional<AxisAlignedBoundingBox> getBoundingBoxForShapeAdt(ShapeAdt shapeAdt, double t0, double t1) {
    return ShapeAdts.caseOf(shapeAdt)
        .BOUNDING_VOLUME_HIERARCHY(boundingVolumeHierarchyModel -> boundingVolumeHierarchyModel.getBoundingBox(t0, t1))
        .BOX(boxModel -> boxModel.getBoundingBox(t0, t1))
        .CONSTANT_MEDIUM(constantMediumModel -> constantMediumModel.getBoundingBox(t0, t1))
        .MOVING_SPHERE(movingSphereModel -> movingSphereModel.getBoundingBox(t0, t1))
        .REVERSE_NORMALS(reverseNormalsModel -> reverseNormalsModel.getBoundingBox(t0, t1))
        .SCENE_OBJECTS_LIST(sceneObjectsList -> sceneObjectsList.getBoundingBox(t0, t1))
        .SPHERE(sphereModel -> sphereModel.getBoundingBox(t0, t1))
        .TRANSLATION(translationModel -> translationModel.getBoundingBox(t0, t1))
        .XY_RECTANGLE(xyRectangleModel -> xyRectangleModel.getBoundingBox(t0, t1))
        .XZ_RECTANGLE(xzRectangleModel -> xzRectangleModel.getBoundingBox(t0, t1))
        .YZ_RECTANGLE(yzRectangleModel -> yzRectangleModel.getBoundingBox(t0, t1))
        .Y_ROTATION(yRotationModel -> yRotationModel.getBoundingBox(t0, t1));
  }

  private static Optional<HitRecord> hitForShapeAdt(ShapeAdt shapeAdt, Ray ray, double tMin, double tMax) {
    return ShapeAdts.caseOf(shapeAdt)
        .BOUNDING_VOLUME_HIERARCHY(boundingVolumeHierarchyModel -> boundingVolumeHierarchyModel.hit(ray, tMin, tMax))
        .BOX(boxModel -> boxModel.hit(ray, tMin, tMax))
        .CONSTANT_MEDIUM(constantMediumModel -> constantMediumModel.hit(ray, tMin, tMax))
        .MOVING_SPHERE(movingSphereModel -> movingSphereModel.hit(ray, tMin, tMax))
        .REVERSE_NORMALS(reverseNormalsModel -> reverseNormalsModel.hit(ray, tMin, tMax))
        .SCENE_OBJECTS_LIST(sceneObjectsList -> sceneObjectsList.hit(ray, tMin, tMax))
        .SPHERE(sphereModel -> sphereModel.hit(ray, tMin, tMax))
        .TRANSLATION(translationModel -> translationModel.hit(ray, tMin, tMax))
        .XY_RECTANGLE(xyRectangleModel -> xyRectangleModel.hit(ray, tMin, tMax))
        .XZ_RECTANGLE(xzRectangleModel -> xzRectangleModel.hit(ray, tMin, tMax))
        .YZ_RECTANGLE(yzRectangleModel -> yzRectangleModel.hit(ray, tMin, tMax))
        .Y_ROTATION(yRotationModel -> yRotationModel.hit(ray, tMin, tMax));
  }

}
