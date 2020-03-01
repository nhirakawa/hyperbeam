package com.github.nhirakawa.hyperbeam.shape;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.IsotropicMaterial;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.hyperbeam.texture.Texture;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public abstract class ConstantMediumModel implements SceneObject {
  private static final Vector3 NORMAL = Vector3
    .builder()
    .setX(1)
    .setY(0)
    .setZ(0)
    .build();

  public abstract SceneObject getSceneObject();

  public abstract double getDensity();

  public abstract Texture getTexture();

  @Value.Derived
  @JsonIgnore
  public Material getPhaseFunction() {
    return IsotropicMaterial.builder().setTexture(getTexture()).build();
  }

  @JsonIgnore
  public static Vector3 normal() {
    return NORMAL;
  }

  @Override
  public SceneObjectType getShapeType() {
    return SceneObjectType.CONSTANT_MEDIUM;
  }
}
