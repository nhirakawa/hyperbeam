package com.github.nhirakawa.hyperbeam;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.util.MathUtils;
import com.google.common.collect.ImmutableList;

public class SortedHittablesFactory {

  private final RayProcessor rayProcessor;

  public SortedHittablesFactory(RayProcessor rayProcessor) {
    this.rayProcessor = rayProcessor;
  }

  private static final Function<AxisAlignedBoundingBox, Double> MIN_X = aabb -> aabb.getMin().getX();
  private static final Function<AxisAlignedBoundingBox, Double> MIN_Y = aabb -> aabb.getMin().getY();
  private static final Function<AxisAlignedBoundingBox, Double> MIN_Z = aabb -> aabb.getMin().getZ();

  List<SceneObject> getSortedHittables(Collection<SceneObject> sceneObjects) {
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

    return sceneObjects.stream()
        .sorted(comparator)
        .collect(ImmutableList.toImmutableList());
  }

  private Comparator<SceneObject> getComparator(Function<AxisAlignedBoundingBox, Double> function) {
    return (hittable1, hittable2) -> {
      Optional<AxisAlignedBoundingBox> box1 = rayProcessor.getBoundingBox(hittable1, 0, 0);
      Optional<AxisAlignedBoundingBox> box2 = rayProcessor.getBoundingBox(hittable2, 0, 0);

      if (function.apply(box1.get()) - function.apply(box2.get()) < 0) {
        return -1;
      } else {
        return 1;
      }
    };
  }


}
