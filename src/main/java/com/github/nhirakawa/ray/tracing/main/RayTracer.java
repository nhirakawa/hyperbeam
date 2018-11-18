package com.github.nhirakawa.ray.tracing.main;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.github.nhirakawa.ray.tracing.camera.Camera;
import com.github.nhirakawa.ray.tracing.color.Rgb;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.image.PpmWriter;
import com.github.nhirakawa.ray.tracing.material.DielectricMaterial;
import com.github.nhirakawa.ray.tracing.material.LambertianMaterial;
import com.github.nhirakawa.ray.tracing.material.MaterialScatterRecord;
import com.github.nhirakawa.ray.tracing.material.MetalMaterial;
import com.github.nhirakawa.ray.tracing.shape.HitRecord;
import com.github.nhirakawa.ray.tracing.shape.Hittable;
import com.github.nhirakawa.ray.tracing.shape.HittablesList;
import com.github.nhirakawa.ray.tracing.shape.Sphere;
import com.google.common.collect.ImmutableList;

public class RayTracer {

  private static final String FILENAME = "test.ppm";

  private static final int MULTIPLIER = 4;

  public static void main(String... args) throws Exception {

    int numberOfRows = 200 * MULTIPLIER;
    int numberOfColumns = 100 * MULTIPLIER;

    List<Rgb> rgbs = buildAntiAliasedSpheres(numberOfRows, numberOfColumns);

    new PpmWriter().write(new File(FILENAME), numberOfRows, numberOfColumns, rgbs);
  }

  private static List<Rgb> buildAntiAliasedSpheres(int numberOfRows, int numberOfColumns) {

    int numberOfSamples = 100;

    Hittable sphere1 = new Sphere(new Vector3(0, 0, -1), 0.5, new LambertianMaterial(new Vector3(0.1, 0.2, 0.5)));
    Hittable sphere2 = new Sphere(new Vector3(0, -100.5, -1), 100, new LambertianMaterial(new Vector3(0.8, 0.8, 0.0)));
    Hittable sphere3 = new Sphere(new Vector3(1, 0, -1), 0.5, new MetalMaterial(new Vector3(0.8, 0.6, 0.2), 0.3));
    Hittable sphere4 = new Sphere(new Vector3(-1, 0, -1), 0.5, new DielectricMaterial(1.5));
    Hittable sphere5 = new Sphere(new Vector3(-1, 0, -1), -0.45, new DielectricMaterial(1.5));

//    double r = StrictMath.cos(Math.PI / 4);
//    Hittable sphere1 = new Sphere(new Vector3(-r, 0, -1), r, new LambertianMaterial(new Vector3(0, 0, 1)));
//    Hittable sphere2 = new Sphere(new Vector3(r, 0, -1), r, new LambertianMaterial(new Vector3(1, 0, 0)));

    HittablesList world = new HittablesList(ImmutableList.of(sphere1, sphere2, sphere3, sphere4, sphere5));
//    HittablesList world = new HittablesList(ImmutableList.of(sphere1, sphere2));

    Vector3 lookFrom = new Vector3(3, 3, 2);
    Vector3 lookAt = new Vector3(0, 0, -1);
    Vector3 viewUp = new Vector3(0, 1, 0);
    double distanceToFocus = lookFrom.subtract(lookAt).getNorm();
    double aperture = 2;
    double aspectRatio = (double) numberOfRows / (double) numberOfColumns;
    Camera camera = new Camera(lookFrom, lookAt, viewUp, 20, aspectRatio, aperture, distanceToFocus);

    List<Rgb> rgbs = new ArrayList<>();
    for (int j = numberOfColumns; j >= 0; j--) {
      for (int i = 0; i < numberOfRows; i++) {
        Vector3 color = new Vector3(0, 0, 0);

        for (int s = 0; s < numberOfSamples; s++) {
          double u = ((double) (i + rand()) / numberOfRows);
          double v = ((double) (j + rand()) / numberOfColumns);

          Ray ray = camera.getRay(u, v);

          Vector3 point = ray.getPointAtParameter(2);

          color = color.add(color(ray, world, 0));
        }

        color = color.scalarDivide(numberOfSamples);
        color = color.apply(Math::sqrt);

        int red = (int) (255.99 * color.getRed());
        int green = (int) (255.99 * color.getGreen());
        int blue = (int) (255.99 * color.getBlue());

        rgbs.add(new Rgb(red, green, blue));
      }
    }

    return Collections.unmodifiableList(rgbs);
  }

  private static Vector3 color(Ray ray, Hittable hittable, int depth) {

    Optional<HitRecord> maybeHitRecord = hittable.hit(ray, 0.001, Double.MAX_VALUE);
    if (maybeHitRecord.isPresent()) {
      MaterialScatterRecord materialScatterRecord = maybeHitRecord.get()
          .getMaterial()
          .scatter(ray, maybeHitRecord.get());

      if (depth < 50 && materialScatterRecord.isWasScattered()) {
        return materialScatterRecord.getAttenuation()
            .multiply(color(materialScatterRecord.getScattered(), hittable, depth + 1));
      } else {
        return Vector3.zero();
      }
    } else {
      Vector3 unitDirection = ray.getDirection().unit();
      double t = 0.5 * (unitDirection.getY() + 1);
      return new Vector3(1, 1, 1).scalarMultiply(1 - t).add(new Vector3(0.5, 0.7, 1).scalarMultiply(t));
    }
  }

  private static Vector3 getRandomUnitSphereVector() {

    while (true) {
      Vector3 point = new Vector3(rand(), rand(), rand()).scalarMultiply(2).subtract(new Vector3(1, 1, 1));
      if (BigDecimal.valueOf(point.getSquaredLength()).compareTo(BigDecimal.ONE) < 0) {
        return point;
      }
    }
  }

  private static double rand() {

    return ThreadLocalRandom.current().nextDouble();
  }

}
