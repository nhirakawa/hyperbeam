package com.github.nhirakawa.hyperbeam.shape;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.nhirakawa.hyperbeam.AlgebraicSceneObject;
import com.github.nhirakawa.hyperbeam.transform.Translation;
import com.github.nhirakawa.hyperbeam.transform.YRotation;

@SuppressWarnings("ClassReferencesSubclass")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "shapeType")
@JsonSubTypes({
    @Type(value = Sphere.class, name = "SPHERE"),
    @Type(value = MovingSphere.class, name = "MOVING_SPHERE"),
    @Type(value = XYRectangle.class, name = "XY_RECTANGLE"),
    @Type(value = YZRectangle.class, name = "YZ_RECTANGLE"),
    @Type(value = XZRectangle.class, name = "XZ_RECTANGLE"),
    @Type(value = ReverseNormals.class, name = "REVERSE_NORMALS"),
    @Type(value = Box.class, name = "BOX"),
    @Type(value = Translation.class, name = "TRANSLATION"),
    @Type(value = YRotation.class, name = "Y_ROTATION"),
    @Type(value = ConstantMedium.class, name = "CONSTANT_MEDIUM"),
    @Type(value = BoundingVolumeHierarchy.class, name = "BOUNDING_VOLUME_HIERARCHY"),
    @Type(value = SceneObjectsList.class, name = "SCENE_OBJECTS_LIST")
})
public interface SceneObject {

  @SuppressWarnings("unused")
  SceneObjectType getShapeType();

  AlgebraicSceneObject toAlgebraicSceneObject();

}
