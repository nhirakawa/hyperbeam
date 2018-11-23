package com.github.nhirakawa.ray.tracing.shape;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.github.nhirakawa.ray.tracing.collision.Hittable;

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "shapeType")
@JsonSubTypes({
    @Type(value = Sphere.class, name = "SPHERE"),
    @Type(value = MovingSphere.class, name = "MOVING_SPHERE"),
    @Type(value = XYRectangle.class, name = "XY_RECTANGLE"),
    @Type(value = YZRectangle.class, name = "YZ_RECTANGLE"),
    @Type(value = XZRectangle.class, name = "XZ_RECTANGLE"),
    @Type(value = ReverseNormals.class, name = "REVERSE_NORMALS")
})
public interface Shape extends Hittable {

  ShapeType getShapeType();

}
