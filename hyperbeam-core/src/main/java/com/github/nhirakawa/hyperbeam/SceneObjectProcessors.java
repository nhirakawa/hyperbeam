package com.github.nhirakawa.hyperbeam;

import java.util.Optional;

import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdt;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdts;

public final class SceneObjectProcessors {

  private SceneObjectProcessors() {}

  public static Optional<HitRecord> hit(ShapeAdt shapeAdt, HitRecordParams hitRecordParams) {
    return ShapeAdts.caseOf(shapeAdt)
        .BOUNDING_VOLUME_HIERARCHY(boundingVolumeHierarchyModel -> BoundingVolumeHierarchyProcessor.hit(boundingVolumeHierarchyModel, hitRecordParams))
        .BOX(boxModel -> BoxProcessor.hit(boxModel, hitRecordParams))
        .CONSTANT_MEDIUM(constantMediumModel -> ConstantMediumProcessor.hit(constantMediumModel, hitRecordParams))
        .MOVING_SPHERE(movingSphereModel -> MovingSphereProcessor.hit(movingSphereModel, hitRecordParams))
        .REVERSE_NORMALS(reverseNormalsModel -> ReverseNormalsProcessor.hit(reverseNormalsModel, hitRecordParams))
        .SCENE_OBJECTS_LIST(sceneObjectsList -> SceneObjectsListProcessor.hit(sceneObjectsList, hitRecordParams))
        .SPHERE(sphereModel -> SphereProcessor.hit(sphereModel, hitRecordParams))
        .TRANSLATION(translationModel -> TranslationProcessor.hit(translationModel, hitRecordParams))
        .XY_RECTANGLE(xyRectangleModel -> XYRectangleProcessor.hit(xyRectangleModel, hitRecordParams))
        .XZ_RECTANGLE(xzRectangleModel -> XZRectangleProcessor.hit(xzRectangleModel, hitRecordParams))
        .YZ_RECTANGLE(yzRectangleModel -> YZRectangleProcessor.hit(yzRectangleModel, hitRecordParams))
        .Y_ROTATION(yRotationModel -> YRotationProcessor.hit(yRotationModel, hitRecordParams));
  }

  public static Optional<AxisAlignedBoundingBox> getBoundingBox(ShapeAdt shapeAdt, BoundingBoxParams boundingBoxParams) {
    throw new UnsupportedOperationException();
  }

}
