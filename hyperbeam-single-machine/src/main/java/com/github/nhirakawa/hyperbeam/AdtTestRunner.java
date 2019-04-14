package com.github.nhirakawa.hyperbeam;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.LambertianMaterial;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdt;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdts;
import com.github.nhirakawa.hyperbeam.shape.Sphere;
import com.github.nhirakawa.hyperbeam.texture.ConstantTexture;

public class AdtTestRunner {

  private static final Logger LOG = LoggerFactory.getLogger(AdtTestRunner.class);

  private static final ObjectMapper OBJECT_MAPPER = buildObjectMapper();

  public static void main(String... args) throws IOException {
    ConstantTexture texture = ConstantTexture.builder()
        .setColor(
            Vector3.builder()
                .setX(50)
                .setY(50)
                .setZ(50)
                .build()
        )
        .build();

    LambertianMaterial material = LambertianMaterial.builder()
        .setTexture(texture)
        .build();

    Sphere sphere = Sphere.builder()
        .setCenter(Vector3.zero())
        .setRadius(1)
        .setMaterial(material)
        .build();

    ShapeAdt serialized = ShapeAdts.SPHERE(sphere);

    String json = OBJECT_MAPPER.writeValueAsString(serialized);

    ShapeAdt deserialized = OBJECT_MAPPER.readValue(json, ShapeAdt.class);
  }

  private static ObjectMapper buildObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new GuavaModule());
    return objectMapper;
  }

}
