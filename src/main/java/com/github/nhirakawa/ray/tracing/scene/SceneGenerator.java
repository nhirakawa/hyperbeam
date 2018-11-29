package com.github.nhirakawa.ray.tracing.scene;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.github.nhirakawa.ray.tracing.camera.Camera;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.material.DiffuseLightMaterial;
import com.github.nhirakawa.ray.tracing.material.LambertianMaterial;
import com.github.nhirakawa.ray.tracing.material.Material;
import com.github.nhirakawa.ray.tracing.shape.Box;
import com.github.nhirakawa.ray.tracing.shape.ConstantMedium;
import com.github.nhirakawa.ray.tracing.shape.ReverseNormals;
import com.github.nhirakawa.ray.tracing.shape.SceneObject;
import com.github.nhirakawa.ray.tracing.shape.Sphere;
import com.github.nhirakawa.ray.tracing.shape.XYRectangle;
import com.github.nhirakawa.ray.tracing.shape.XZRectangle;
import com.github.nhirakawa.ray.tracing.shape.YZRectangle;
import com.github.nhirakawa.ray.tracing.texture.ConstantTexture;
import com.github.nhirakawa.ray.tracing.texture.ImageTexture;
import com.github.nhirakawa.ray.tracing.texture.PerlinNoiseTexture;
import com.github.nhirakawa.ray.tracing.texture.Texture;
import com.github.nhirakawa.ray.tracing.transform.Translation;
import com.github.nhirakawa.ray.tracing.transform.YRotation;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;

@SuppressWarnings("MagicNumber")
public final class SceneGenerator {

  private SceneGenerator() {}

  private static final ObjectMapper OBJECT_MAPPER = buildObjectMapper();

  private static final Camera COMMON_CAMERA = Camera.builder()
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

    Texture texture = PerlinNoiseTexture.builder()
        .setScale(0.3)
        .build();
    SceneObject sphere1 = Sphere.builder()
        .setCenter(new Vector3(0, -1000, 0))
        .setRadius(1000)
        .setMaterial(
            LambertianMaterial.builder()
                .setTexture(texture)
                .build()
        )
        .build();
    SceneObject sphere2 = Sphere.builder()
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
        .addSceneObjects(sphere1)
        .addSceneObjects(sphere2)
        .build();
  }

  public static Scene generateEarth() {
    Material material = LambertianMaterial.builder()
        .setTexture(
            ImageTexture.builder()
                .setImageUrl(Resources.getResource("textures/earth-8k.jpg"))
                .build()
        )
        .build();

    Sphere sphere = Sphere.builder()
        .setCenter(new Vector3(0, 0, 0))
        .setRadius(2)
        .setMaterial(material)
        .build();

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

    return Scene.builder()
        .setCamera(camera)
        .addSceneObjects(sphere)
        .build();
  }

  public static Scene generateSphereAndLight() {
    Texture texture = PerlinNoiseTexture.builder()
        .setScale(4)
        .build();

    List<SceneObject> sceneObjects = ImmutableList.of(
        Sphere.builder()
            .setCenter(new Vector3(0, -1000, 0))
            .setRadius(1000)
            .setMaterial(
                LambertianMaterial.builder()
                    .setTexture(texture)
                    .build()
            )
            .build(),
        Sphere.builder()
            .setCenter(new Vector3(0, 2, 0))
            .setRadius(2)
            .setMaterial(
                LambertianMaterial.builder()
                    .setTexture(texture)
                    .build()
            )
            .build(),
        Sphere.builder()
            .setCenter(new Vector3(0, 7, 0))
            .setRadius(2)
            .setMaterial(
                DiffuseLightMaterial.builder()
                    .setTexture(
                        ConstantTexture.builder()
                            .setColor(new Vector3(4, 4, 4))
                            .build()
                    )
                    .build()
            )
            .build(),
        XYRectangle.builder()
            .setX0(3)
            .setX1(5)
            .setY0(1)
            .setY1(3)
            .setK(-2)
            .setMaterial(
                DiffuseLightMaterial.builder()
                    .setTexture(
                        ConstantTexture.builder()
                            .setColor(new Vector3(4, 4, 4))
                            .build()
                    )
                    .build()
            )
            .build()
    );

    return Scene.builder()
        .setCamera(COMMON_CAMERA)
        .addAllSceneObjects(sceneObjects)
        .build();
  }

  public static Scene generateCornellBox() {
    Camera camera = Camera.builder()
        .setLookFrom(new Vector3(278, 278, -800))
        .setLookAt(new Vector3(278, 278, 0))
        .setFocusDistance(10)
        .setAperture(0)
        .setVerticalFovDegrees(40)
        .setViewUp(new Vector3(0, 1, 0))
        .setAspectRatio(2)
        .setTime0(0)
        .setTime1(1)
        .build();

    Material red = LambertianMaterial.builder()
        .setTexture(
            ConstantTexture.builder()
                .setColor(new Vector3(0.65, 0.05, 0.05))
                .build()
        )
        .build();
    Material white = LambertianMaterial.builder()
        .setTexture(
            ConstantTexture.builder()
                .setColor(new Vector3(0.73, 0.73, 0.73))
                .build()
        )
        .build();
    Material green = LambertianMaterial.builder()
        .setTexture(
            ConstantTexture.builder()
                .setColor(new Vector3(0.12, 0.45, 0.15))
                .build()
        )
        .build();
    Material light = DiffuseLightMaterial.builder()
        .setTexture(
            ConstantTexture.builder()
                .setColor(new Vector3(15, 15, 15))
                .build()
        )
        .build();

    List<SceneObject> sceneObjects = ImmutableList.of(
        ReverseNormals.builder()
            .setSceneObject(
                YZRectangle.builder()
                    .setY0(0)
                    .setY1(555)
                    .setZ0(0)
                    .setZ1(555)
                    .setK(555)
                    .setMaterial(green)
                    .build()
            )
            .build(),
        YZRectangle.builder()
            .setY0(0)
            .setY1(555)
            .setZ0(0)
            .setZ1(555)
            .setK(0)
            .setMaterial(red)
            .build(),
        XZRectangle.builder()
            .setX0(213)
            .setX1(343)
            .setZ0(227)
            .setZ1(332)
            .setK(554)
            .setMaterial(light)
            .build(),
        ReverseNormals.builder()
            .setSceneObject(
                XZRectangle.builder()
                    .setX0(0)
                    .setX1(555)
                    .setZ0(0)
                    .setZ1(555)
                    .setK(555)
                    .setMaterial(white)
                    .build()
            )
            .build(),
        XZRectangle.builder()
            .setX0(0)
            .setX1(555)
            .setZ0(0)
            .setZ1(555)
            .setK(0)
            .setMaterial(white)
            .build(),
        ReverseNormals.builder()
            .setSceneObject(
                XYRectangle.builder()
                    .setX0(0)
                    .setX1(555)
                    .setY0(0)
                    .setY1(555)
                    .setK(555)
                    .setMaterial(white)
                    .build()
            )
            .build(),
        Translation.builder()
            .setSceneObject(
                YRotation.builder()
                    .setSceneObject(
                        Box.builder()
                            .setPMin(Vector3.zero())
                            .setPMax(new Vector3(165, 165, 165))
                            .setMaterial(white)
                            .build()
                    )
                    .setAngleInDegrees(-18)
                    .build()
            )
            .setOffset(new Vector3(130, 0, 65))
            .build(),
        Translation.builder()
            .setSceneObject(
                YRotation.builder()
                    .setSceneObject(
                        Box.builder()
                            .setPMin(Vector3.zero())
                            .setPMax(new Vector3(165, 330, 165))
                            .setMaterial(white)
                            .build()
                    )
                    .setAngleInDegrees(15)
                    .build()
            )
            .setOffset(new Vector3(265, 0, 295))
            .build()
    );

    return Scene.builder()
        .setCamera(camera)
        .addAllSceneObjects(sceneObjects)
        .build();
  }

  public static Scene generateCornellSmoke() {
    Camera camera = Camera.builder()
        .setLookFrom(new Vector3(278, 278, -800))
        .setLookAt(new Vector3(278, 278, 0))
        .setFocusDistance(10)
        .setAperture(0)
        .setVerticalFovDegrees(40)
        .setViewUp(new Vector3(0, 1, 0))
        .setAspectRatio(2)
        .setTime0(0)
        .setTime1(1)
        .build();

    Material red = LambertianMaterial.builder()
        .setTexture(
            ConstantTexture.builder()
                .setColor(new Vector3(0.65, 0.05, 0.05))
                .build()
        )
        .build();
    Material white = LambertianMaterial.builder()
        .setTexture(
            ConstantTexture.builder()
                .setColor(new Vector3(0.73, 0.73, 0.73))
                .build()
        )
        .build();
    Material green = LambertianMaterial.builder()
        .setTexture(
            ConstantTexture.builder()
                .setColor(new Vector3(0.12, 0.45, 0.15))
                .build()
        )
        .build();
    Material light = DiffuseLightMaterial.builder()
        .setTexture(
            ConstantTexture.builder()
                .setColor(new Vector3(15, 15, 15))
                .build()
        )
        .build();

    List<SceneObject> sceneObjects = ImmutableList.of(
        ReverseNormals.builder()
            .setSceneObject(
                YZRectangle.builder()
                    .setY0(0)
                    .setY1(555)
                    .setZ0(0)
                    .setZ1(555)
                    .setK(555)
                    .setMaterial(green)
                    .build()
            )
            .build(),
        YZRectangle.builder()
            .setY0(0)
            .setY1(555)
            .setZ0(0)
            .setZ1(555)
            .setK(0)
            .setMaterial(red)
            .build(),
        XZRectangle.builder()
            .setX0(213)
            .setX1(343)
            .setZ0(227)
            .setZ1(332)
            .setK(554)
            .setMaterial(light)
            .build(),
        ReverseNormals.builder()
            .setSceneObject(
                XZRectangle.builder()
                    .setX0(0)
                    .setX1(555)
                    .setZ0(0)
                    .setZ1(555)
                    .setK(555)
                    .setMaterial(white)
                    .build()
            )
            .build(),
        XZRectangle.builder()
            .setX0(0)
            .setX1(555)
            .setZ0(0)
            .setZ1(555)
            .setK(0)
            .setMaterial(white)
            .build(),
        ReverseNormals.builder()
            .setSceneObject(
                XYRectangle.builder()
                    .setX0(0)
                    .setX1(555)
                    .setY0(0)
                    .setY1(555)
                    .setK(555)
                    .setMaterial(white)
                    .build()
            )
            .build(),
        ConstantMedium.builder()
            .setSceneObject(
                Translation.builder()
                    .setSceneObject(
                        YRotation.builder()
                            .setSceneObject(
                                Box.builder()
                                    .setPMin(Vector3.zero())
                                    .setPMax(new Vector3(165, 165, 165))
                                    .setMaterial(white)
                                    .build()
                            )
                            .setAngleInDegrees(-18)
                            .build()
                    )
                    .setOffset(new Vector3(130, 0, 65))
                    .build()
            )
            .setDensity(0.01)
            .setTexture(
                ConstantTexture.builder()
                    .setColor(new Vector3(1, 1, 1))
                    .build()
            )
            .build(),
        ConstantMedium.builder()
            .setSceneObject(
                Translation.builder()
                    .setSceneObject(
                        YRotation.builder()
                            .setSceneObject(
                                Box.builder()
                                    .setPMin(Vector3.zero())
                                    .setPMax(new Vector3(165, 330, 165))
                                    .setMaterial(white)
                                    .build()
                            )
                            .setAngleInDegrees(15)
                            .build()
                    )
                    .setOffset(new Vector3(265, 0, 295))
                    .build()
            )
            .setDensity(0.01)
            .setTexture(
                ConstantTexture.builder()
                    .setColor(Vector3.zero())
                    .build()
            )
            .build()
    );

    return Scene.builder()
        .setCamera(camera)
        .addAllSceneObjects(sceneObjects)
        .build();
  }

  private static ObjectMapper buildObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new GuavaModule());
    return objectMapper;
  }

  public static void main(String... args) throws IOException {
    writeScene(generateTwoPerlinSpheres(), "two-perlin-spheres.json");
    writeScene(generateCornellSmoke(), "cornell-smoke.json");
    writeScene(generateCornellBox(), "cornell-box.json");
    writeScene(generateEarth(), "earth.json");
    writeScene(generateSphereAndLight(), "sphere-and-light.json");
  }

  private static void writeScene(Scene scene, String filename) throws IOException {
    File file = new File("scenes");

    if (!file.exists()) {
      file.mkdir();
    }

    try (FileWriter fileWriter = new FileWriter("scenes/" + filename)) {
      fileWriter.write(OBJECT_MAPPER.writeValueAsString(scene));
    }
  }

}
