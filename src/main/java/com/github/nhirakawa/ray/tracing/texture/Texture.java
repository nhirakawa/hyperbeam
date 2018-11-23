package com.github.nhirakawa.ray.tracing.texture;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "textureType")
@JsonSubTypes({
    @Type(value = CheckerTexture.class, name = "CHECKER"),
    @Type(value = ConstantTexture.class, name = "CONSTANT"),
    @Type(value = PerlinNoiseTexture.class, name = "PERLIN_NOISE"),
    @Type(value = ImageTexture.class, name = "IMAGE")
})
public interface Texture {

  Vector3 getValue(double u, double v, Vector3 point);
  TextureType getTextureType();

}
