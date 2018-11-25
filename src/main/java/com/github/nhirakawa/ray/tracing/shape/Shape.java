package com.github.nhirakawa.ray.tracing.shape;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.nhirakawa.ray.tracing.collision.Hittable;
import com.github.nhirakawa.ray.tracing.transform.Translation;
import com.github.nhirakawa.ray.tracing.transform.YRotation;

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
    @Type(value = ConstantMedium.class, name = "CONSTANT_MEDIUM")
})
public interface Shape extends Hittable {

  @SuppressWarnings("unused")
  ShapeType getShapeType();

}
