package com.github.nhirakawa.hyperbeam.scene;

import static com.github.nhirakawa.hyperbeam.util.MathUtils.rand;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.github.nhirakawa.hyperbeam.camera.Camera;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.DielectricMaterial;
import com.github.nhirakawa.hyperbeam.material.DiffuseLightMaterial;
import com.github.nhirakawa.hyperbeam.material.LambertianMaterial;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.hyperbeam.material.MetalMaterial;
import com.github.nhirakawa.hyperbeam.shape.BoundingVolumeHierarchy;
import com.github.nhirakawa.hyperbeam.shape.Box;
import com.github.nhirakawa.hyperbeam.shape.ConstantMedium;
import com.github.nhirakawa.hyperbeam.shape.MovingSphere;
import com.github.nhirakawa.hyperbeam.shape.ReverseNormals;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.shape.Sphere;
import com.github.nhirakawa.hyperbeam.shape.XYRectangle;
import com.github.nhirakawa.hyperbeam.shape.XZRectangle;
import com.github.nhirakawa.hyperbeam.shape.YZRectangle;
import com.github.nhirakawa.hyperbeam.texture.ConstantTexture;
import com.github.nhirakawa.hyperbeam.texture.ImageTexture;
import com.github.nhirakawa.hyperbeam.texture.PerlinNoiseTexture;
import com.github.nhirakawa.hyperbeam.texture.Texture;
import com.github.nhirakawa.hyperbeam.transform.Translation;
import com.github.nhirakawa.hyperbeam.transform.YRotation;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;

@SuppressWarnings("MagicNumber")
public final class SceneGenerator {

	private SceneGenerator() {}

	private static final ObjectMapper OBJECT_MAPPER = buildObjectMapper();

	private static final Camera COMMON_CAMERA = Camera.builder()
			.setLookFrom(
					Vector3.builder()
							.setX(13)
							.setY(2)
							.setZ(3)
							.build()
			)
			.setLookAt(Vector3.zero())
			.setViewUp(
					Vector3.builder()
							.setX(0)
							.setY(1)
							.setZ(0)
							.build()
			)
			.setFocusDistance(10)
			.setAperture(0)
			.setAspectRatio(200 / 100)
			.setTime0(0)
			.setTime1(1)
			.setVerticalFovDegrees(20)
			.build();

	public static Scene generateTwoPerlinSpheres() {
		Texture texture = PerlinNoiseTexture.builder()
				.setScale(0.3)
				.build();
		SceneObject sphere1 = Sphere.builder()
				.setCenter(
						Vector3.builder()
								.setX(0)
								.setY(-1000)
								.setZ(0)
								.build()
				)
				.setRadius(1000)
				.setMaterial(
						LambertianMaterial.builder()
								.setTexture(texture)
								.build()
				)
				.build();
		SceneObject sphere2 = Sphere.builder()
				.setCenter(
						Vector3.builder()
								.setX(0)
								.setY(2)
								.setZ(0)
								.build()
				)
				.setRadius(2)
				.setMaterial(
						LambertianMaterial.builder()
								.setTexture(texture)
								.build()
				)
				.build();

		return Scene.builder()
				.setCamera(COMMON_CAMERA)
				.addSceneObjects(sphere1)
				.addSceneObjects(sphere2)
				.build();
	}

	public static Scene generateEarth() {
		Material material = LambertianMaterial.builder()
				.setTexture(
						ImageTexture.builder()
								.setImageUrl(Resources.getResource("src/src/main/resources/textures/earth-8k.jpg"))
								.build()
				)
				.build();

		Sphere sphere = Sphere.builder()
				.setCenter(Vector3.zero())
				.setRadius(2)
				.setMaterial(material)
				.build();

		return Scene.builder()
				.setCamera(COMMON_CAMERA)
				.addSceneObjects(sphere)
				.build();
	}

	public static Scene generateSphereAndLight() {
		Texture texture = PerlinNoiseTexture.builder()
				.setScale(4)
				.build();

		List<SceneObject> sceneObjects = ImmutableList.of(
				Sphere.builder()
						.setCenter(
								Vector3.builder()
										.setX(0)
										.setY(-1000)
										.setZ(0)
										.build()
						)
						.setRadius(1000)
						.setMaterial(
								LambertianMaterial.builder()
										.setTexture(texture)
										.build()
						)
						.build(),
				Sphere.builder()
						.setCenter(
								Vector3.builder()
										.setX(0)
										.setY(2)
										.setZ(0)
										.build()
						)
						.setRadius(2)
						.setMaterial(
								LambertianMaterial.builder()
										.setTexture(texture)
										.build()
						)
						.build(),
				Sphere.builder()
						.setCenter(
								Vector3.builder()
										.setX(0)
										.setY(7)
										.setZ(0)
										.build()
						)
						.setRadius(2)
						.setMaterial(
								DiffuseLightMaterial.builder()
										.setTexture(
												ConstantTexture.builder()
														.setColor(
																Vector3.builder()
																		.setX(4)
																		.setY(4)
																		.setZ(4)
																		.build()
														)
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
														.setColor(
																Vector3.builder()
																		.setX(4)
																		.setY(4)
																		.setZ(4)
																		.build()
														)
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
				.setLookFrom(
						Vector3.builder()
								.setX(278)
								.setY(278)
								.setZ(-800)
								.build()
				)
				.setLookAt(
						Vector3.builder()
								.setX(278)
								.setY(278)
								.setZ(0)
								.build()
				)
				.setFocusDistance(10)
				.setAperture(0)
				.setVerticalFovDegrees(40)
				.setViewUp(
						Vector3.builder()
								.setX(0)
								.setY(1)
								.setZ(0)
								.build()
				)
				.setAspectRatio(2)
				.setTime0(0)
				.setTime1(1)
				.build();

		Material red = LambertianMaterial.builder()
				.setTexture(
						ConstantTexture.builder()
								.setColor(
										Vector3.builder()
												.setX(0.65)
												.setY(0.05)
												.setZ(0.05)
												.build()
								)
								.build()
				)
				.build();
		Material white = LambertianMaterial.builder()
				.setTexture(
						ConstantTexture.builder()
								.setColor(
										Vector3.builder()
												.setX(0.73)
												.setY(0.73)
												.setZ(0.73)
												.build()
								)
								.build()
				)
				.build();
		Material green = LambertianMaterial.builder()
				.setTexture(
						ConstantTexture.builder()
								.setColor(
										Vector3.builder()
												.setX(0.12)
												.setY(0.45)
												.setZ(0.15)
												.build()
								)
								.build()
				)
				.build();
		Material light = DiffuseLightMaterial.builder()
				.setTexture(
						ConstantTexture.builder()
								.setColor(
										Vector3.builder()
												.setX(15)
												.setY(15)
												.setZ(15)
												.build()
								)
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
														.setPMax(
																Vector3.builder()
																		.setX(165)
																		.setY(165)
																		.setZ(165)
																		.build()
														)
														.setMaterial(white)
														.build()
										)
										.setAngleInDegrees(-18)
										.build()
						)
						.setOffset(
								Vector3.builder()
										.setX(130)
										.setY(0)
										.setZ(65)
										.build()
						)
						.build(),
				Translation.builder()
						.setSceneObject(
								YRotation.builder()
										.setSceneObject(
												Box.builder()
														.setPMin(Vector3.zero())
														.setPMax(
																Vector3.builder()
																		.setX(165)
																		.setY(330)
																		.setZ(165)
																		.build()
														)
														.setMaterial(white)
														.build()
										)
										.setAngleInDegrees(15)
										.build()
						)
						.setOffset(
								Vector3.builder()
										.setX(265)
										.setY(0)
										.setZ(295)
										.build()
						)
						.build()
		);

		return Scene.builder()
				.setCamera(camera)
				.addAllSceneObjects(sceneObjects)
				.build();
	}

	public static Scene generateCornellSmoke() {
		Camera camera = Camera.builder()
				.setLookFrom(
						Vector3.builder()
								.setX(278)
								.setY(278)
								.setZ(-800)
								.build()
				)
				.setLookAt(
						Vector3.builder()
								.setX(278)
								.setY(278)
								.setZ(0)
								.build()
				)
				.setFocusDistance(10)
				.setAperture(0)
				.setVerticalFovDegrees(40)
				.setViewUp(
						Vector3.builder()
								.setX(0)
								.setY(1)
								.setZ(0)
								.build()
				)
				.setAspectRatio(2)
				.setTime0(0)
				.setTime1(1)
				.build();

		Material red = LambertianMaterial.builder()
				.setTexture(
						ConstantTexture.builder()
								.setColor(
										Vector3.builder()
												.setX(0.65)
												.setY(0.05)
												.setZ(0.05)
												.build()
								)
								.build()
				)
				.build();
		Material white = LambertianMaterial.builder()
				.setTexture(
						ConstantTexture.builder()
								.setColor(
										Vector3.builder()
												.setX(0.73)
												.setY(0.73)
												.setZ(0.73)
												.build()
								)
								.build()
				)
				.build();
		Material green = LambertianMaterial.builder()
				.setTexture(
						ConstantTexture.builder()
								.setColor(
										Vector3.builder()
												.setX(0.12)
												.setY(0.45)
												.setZ(0.15)
												.build()
								)
								.build()
				)
				.build();
		Material light = DiffuseLightMaterial.builder()
				.setTexture(
						ConstantTexture.builder()
								.setColor(
										Vector3.builder()
												.setX(15)
												.setY(15)
												.setZ(15)
												.build()
								)
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
																		.setPMax(
																				Vector3.builder()
																						.setX(165)
																						.setY(165)
																						.setZ(165)
																						.build()
																		)
																		.setMaterial(white)
																		.build()
														)
														.setAngleInDegrees(-18)
														.build()
										)
										.setOffset(
												Vector3.builder()
														.setX(130)
														.setY(0)
														.setZ(65)
														.build()
										)
										.build()
						)
						.setDensity(0.01)
						.setTexture(
								ConstantTexture.builder()
										.setColor(Vector3.one())
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
																		.setPMax(
																				Vector3.builder()
																						.setX(165)
																						.setY(330)
																						.setZ(165)
																						.build()
																		)
																		.setMaterial(white)
																		.build()
														)
														.setAngleInDegrees(15)
														.build()
										)
										.setOffset(
												Vector3.builder()
														.setX(265)
														.setY(0)
														.setZ(295)
														.build()
										)
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

	public static Scene generateFinalScene() {
		Material white = LambertianMaterial.builder()
				.setTexture(
						ConstantTexture.builder()
								.setColor(
										Vector3.builder()
												.setX(0.73)
												.setY(0.73)
												.setZ(0.73)
												.build()
								)
								.build()
				)
				.build();

		Material ground = LambertianMaterial.builder()
				.setTexture(
						ConstantTexture.builder()
								.setColor(
										Vector3.builder()
												.setX(0.48)
												.setY(0.83)
												.setZ(0.53)
												.build()
								)
								.build()
				)
				.build();

		List<SceneObject> list = new ArrayList<>(30);
		List<SceneObject> boxList = new ArrayList<>(10_000);
		List<SceneObject> boxList2 = new ArrayList<>(10_000);

		int index = 0;
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				double w = 100;
				double x0 = -1000 + i * w;
				double z0 = -1000 + j * w;
				double y0 = 0;
				double x1 = x0 + w;
				double y1 = 100 * rand() + 0.01;
				double z1 = z0 + w;

				Box box = Box.builder()
						.setPMin(
								Vector3.builder()
										.setX(x0)
										.setY(y0)
										.setZ(z0)
										.build()
						)
						.setPMax(
								Vector3.builder()
										.setX(x1)
										.setY(y1)
										.setZ(z1)
										.build()
						)
						.setMaterial(ground)
						.build();

				boxList.add(index, box);
			}
		}

		list.add(
				BoundingVolumeHierarchy.builder()
						.setSortedSceneObjects(boxList)
						.setTime0(0)
						.setTime1(1)
						.build()
		);

		Material light = DiffuseLightMaterial.builder()
				.setTexture(
						ConstantTexture.builder()
								.setColor(
										Vector3.builder()
												.setX(7)
												.setY(7)
												.setZ(7)
												.build()
								)
								.build()
				)
				.build();

		XZRectangle xzRectangle = XZRectangle.builder()
				.setX0(123)
				.setX1(423)
				.setZ0(147)
				.setZ1(412)
				.setK(554)
				.setMaterial(light)
				.build();

		list.add(xzRectangle);

		Vector3 center = Vector3.builder()
				.setX(400)
				.setY(400)
				.setZ(200)
				.build();

		MovingSphere movingSphere = MovingSphere.builder()
				.setCenter0(center)
				.setCenter1(
						center.add(
								Vector3.builder()
										.setX(30)
										.setY(0)
										.setZ(0)
										.build()
						)
				)
				.setTime0(0)
				.setTime1(1)
				.setRadius(50)
				.setMaterial(
						LambertianMaterial.builder()
								.setTexture(
										ConstantTexture.builder()
												.setColor(
														Vector3.builder()
																.setX(0.7)
																.setY(0.3)
																.setZ(0.1)
																.build()
												)
												.build()
								)
								.build()
				)
				.build();

		list.add(movingSphere);

		list.add(
				Sphere.builder()
						.setCenter(
								Vector3.builder()
										.setX(260)
										.setY(150)
										.setZ(45)
										.build()
						)
						.setRadius(70)
						.setMaterial(
								DielectricMaterial.builder()
										.setRefractiveIndex(1.5)
										.build()
						)
						.build()
		);

		list.add(
				Sphere.builder()
						.setCenter(
								Vector3.builder()
										.setX(0)
										.setY(150)
										.setZ(145)
										.build()
						)
						.setRadius(50)
						.setMaterial(
								MetalMaterial.builder()
										.setAlbedo(
												Vector3.builder()
														.setX(0.8)
														.setY(0.8)
														.setZ(0.9)
														.build()
										)
										.setFuzz(10)
										.build()
						)
						.build()
		);

		SceneObject boundary = Sphere.builder()
				.setCenter(
						Vector3.builder()
								.setX(360)
								.setY(150)
								.setZ(145)
								.build()
				)
				.setRadius(70)
				.setMaterial(
						DielectricMaterial.builder()
								.setRefractiveIndex(1.5)
								.build()
				)
				.build();

		list.add(boundary);

		list.add(
				ConstantMedium.builder()
						.setSceneObject(boundary)
						.setDensity(0.2)
						.setTexture(
								ConstantTexture.builder()
										.setColor(
												Vector3.builder()
														.setX(0.2)
														.setY(0.4)
														.setZ(0.9)
														.build()
										)
										.build()
						)
						.build()
		);

		boundary = Sphere.builder()
				.setCenter(Vector3.zero())
				.setRadius(5000)
				.setMaterial(
						DielectricMaterial.builder()
								.setRefractiveIndex(1.5)
								.build()
				)
				.build();

		list.add(
				ConstantMedium.builder()
						.setSceneObject(boundary)
						.setDensity(0.0001)
						.setTexture(
								ConstantTexture.builder()
										.setColor(
												Vector3.one()
										)
										.build()
						)
						.build()
		);

		list.add(
				Sphere.builder()
						.setCenter(
								Vector3.builder()
										.setX(400)
										.setY(200)
										.setZ(400)
										.build()
						)
						.setRadius(100)
						.setMaterial(
								LambertianMaterial.builder()
										.setTexture(
												ImageTexture.builder()
														.setImageUrl(Resources.getResource("textures/earth-8k.jpg"))
														.build()
										)
										.build()
						)
						.build()
		);

		list.add(
				Sphere.builder()
						.setCenter(
								Vector3.builder()
										.setX(220)
										.setY(280)
										.setZ(300)
										.build()
						)
						.setRadius(80)
						.setMaterial(
								LambertianMaterial.builder()
										.setTexture(
												PerlinNoiseTexture.builder()
														.setScale(0.1)
														.build()
										)
										.build()
						)
						.build()
		);

		for (int i = 0; i < 1000; i++) {
			boxList2.add(
					Sphere.builder()
							.setCenter(
									Vector3.builder()
											.setX(165 * rand())
											.setY(165 * rand())
											.setZ(165 * rand())
											.build()
							)
							.setRadius(10)
							.setMaterial(white)
							.build()
			);
		}

		list.add(
				Translation.builder()
						.setSceneObject(
								YRotation.builder()
										.setSceneObject(
												BoundingVolumeHierarchy.builder()
														.setSortedSceneObjects(boxList2)
														.setTime0(0)
														.setTime1(1)
														.build()
										)
										.setAngleInDegrees(15)
										.build()
						)
						.setOffset(
								Vector3.builder()
										.setX(-100)
										.setY(270)
										.setZ(395)
										.build()
						)
						.build()
		);

		return Scene.builder()
				.setCamera(COMMON_CAMERA)
				.setSceneObjects(list)
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
		File file = new File("src/src/main/resources/scenes");

		if (!file.exists()) {
			file.mkdir();
		}

		try (FileWriter fileWriter = new FileWriter("scenes/" + filename)) {
			fileWriter.write(OBJECT_MAPPER.writeValueAsString(scene));
		}
	}

}

