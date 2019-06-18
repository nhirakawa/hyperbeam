package com.github.nhirakawa.hyperbeam;

import org.derive4j.Data;

import com.github.nhirakawa.hyperbeam.shape.BoundingVolumeHierarchyModel;
import com.github.nhirakawa.hyperbeam.shape.BoxModel;
import com.github.nhirakawa.hyperbeam.shape.ConstantMediumModel;
import com.github.nhirakawa.hyperbeam.shape.MovingSphereModel;
import com.github.nhirakawa.hyperbeam.shape.ReverseNormalsModel;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectsList;
import com.github.nhirakawa.hyperbeam.shape.SphereModel;
import com.github.nhirakawa.hyperbeam.shape.XYRectangleModel;
import com.github.nhirakawa.hyperbeam.shape.XZRectangleModel;
import com.github.nhirakawa.hyperbeam.shape.YZRectangleModel;
import com.github.nhirakawa.hyperbeam.transform.TranslationModel;
import com.github.nhirakawa.hyperbeam.transform.YRotationModel;

@Data
public abstract class AlgebraicSceneObject {

  interface Cases<R> {
    R BOUNDING_VOLUME_HIERARCHY(BoundingVolumeHierarchyModel boundingVolumeHierarchy);
    R BOX(BoxModel box);
    R CONSTANT_MEDIUM(ConstantMediumModel constantMedium);
    R MOVING_SPHERE(MovingSphereModel movingSphere);
    R REVERSE_NORMALS(ReverseNormalsModel reverseNormals);
    R SCENE_OBJECTS_LIST(SceneObjectsList sceneObjectsList);
    R SPHERE(SphereModel sphere);
    R TRANSLATION(TranslationModel translation);
    R XY_RECTANGLE(XYRectangleModel xyRectangle);
    R XZ_RECTANGLE(XZRectangleModel xzRectangle);
    R Y_ROTATION(YRotationModel yRotation);
    R YZ_RECTANGLE(YZRectangleModel yzRectangle);
  }

  public abstract <R> R match(Cases<R> cases);

}
