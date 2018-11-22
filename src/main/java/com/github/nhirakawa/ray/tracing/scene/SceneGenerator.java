package com.github.nhirakawa.ray.tracing.scene;

import java.io.FileWriter;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.github.nhirakawa.ray.tracing.camera.Camera;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.material.LambertianMaterial;
import com.github.nhirakawa.ray.tracing.shape.Shape;
import com.github.nhirakawa.ray.tracing.shape.Sphere;
import com.github.nhirakawa.ray.tracing.texture.PerlinNoiseTexture;
import com.github.nhirakawa.ray.tracing.texture.Texture;

public class SceneGenerator {

  private static final ObjectMapper OBJECT_MAPPER = buildObjectMapper();

  public static Scene generateTwoPerlinSpheres() {
    Camera camera = Camera.builder()
        .setLookFrom(new Vector3(13, 2, 3))
        .setLookAt(new Vector3(0, 0, 0))
        .setViewUp(new Vector3(0, 1, 0))
        .setFocusDistance(10)
        .setAperture(0)
        .setAspectRatio(200 / 100)
        .setTime0(0)
        .setTime1(1)
        .setVerticalFovDegrees(20)
        .build();

    Texture texture = PerlinNoiseTexture.builder().build();
    Shape sphere1 = Sphere.builder()
        .setCenter(new Vector3(0, -1000, 0))
        .setRadius(1000)
        .setMaterial(
            LambertianMaterial.builder()
                .setTexture(texture)
                .build()
        )
        .build();
    Shape sphere2 = Sphere.builder()
        .setCenter(new Vector3(0, 2, 0))
        .setRadius(2)
        .setMaterial(
            LambertianMaterial.builder()
                .setTexture(texture)
                .build()
        )
        .build();

    return Scene.builder()
        .setCamera(camera)
        .addShapes(sphere1)
        .addShapes(sphere2)
        .build();
  }

  private static ObjectMapper buildObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new GuavaModule());
    return objectMapper;
  }

  public static void main(String... args) throws IOException {
    Scene twoPerlinSpheres = generateTwoPerlinSpheres();

    try (FileWriter fileWriter = new FileWriter("two-perlin-spheres.json")) {
      fileWriter.write(OBJECT_MAPPER.writeValueAsString(twoPerlinSpheres));
    }
  }
}
