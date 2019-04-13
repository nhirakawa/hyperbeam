package com.github.nhirakawa.hyperbeam.shape;

import org.derive4j.Data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.nhirakawa.hyperbeam.jackson.ShapeAdtSerializer;

@JsonSerialize(using = ShapeAdtSerializer.class)
@Data
public abstract class ShapeAdt {

  interface Cases<R> {

    R BOUNDING_VOLUME_HIERARCHY(BoundingVolumeHierarchyModel boundingVolumeHierarchy);
    R BOX(BoxModel box);
    R CONSTANT_MEDIUM(ConstantMediumModel constantMedium);
    R MOVING_SPHERE(MovingSphereModel movingSphere);
    R REVERSE_NORMALS(ReverseNormalsModel reverseNormals);
    R SCENE_OBJECTS_LIST(SceneObjectsList sceneObjectsList);
    R SPHERE(SphereModel sphere);
    R XY_RECTANGLE(XYRectangleModel xyRectangle);
    R XZ_RECTANGLE(XZRectangleModel xzRectangle);
    R YZ_RECTANGLE(YZRectangleModel yzRectangle);

  }

  public abstract <R> R match(Cases<R> cases);

}
