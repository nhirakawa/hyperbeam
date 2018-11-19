package com.github.nhirakawa.ray.tracing.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nhirakawa.ray.tracing.camera.Camera;
import com.github.nhirakawa.ray.tracing.color.Rgb;
import com.github.nhirakawa.ray.tracing.color.RgbModel;
import com.github.nhirakawa.ray.tracing.geometry.Coordinates;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.material.DielectricMaterial;
import com.github.nhirakawa.ray.tracing.material.LambertianMaterial;
import com.github.nhirakawa.ray.tracing.material.MaterialScatterRecord;
import com.github.nhirakawa.ray.tracing.material.MetalMaterial;
import com.github.nhirakawa.ray.tracing.shape.HitRecord;
import com.github.nhirakawa.ray.tracing.shape.Hittable;
import com.github.nhirakawa.ray.tracing.shape.HittablesList;
import com.github.nhirakawa.ray.tracing.shape.Sphere;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class RayTracer {

  private static final Logger LOG = LoggerFactory.getLogger(RayTracer.class);

  private static final String FILENAME = "test.png";

  private static final int MULTIPLIER = 2;

  public static void main(String... args) throws Exception {
    int numberOfRows = 200 * MULTIPLIER;
    int numberOfColumns = 100 * MULTIPLIER;

    doThreadedRayTrace(numberOfRows, numberOfColumns, Runtime.getRuntime().availableProcessors());
  }

  private static void doThreadedRayTrace(int numberOfRows,
                                         int numberOfColumns,
                                         int numberOfThreads) throws IOException {
    Stopwatch stopwatch = Stopwatch.createStarted();
    List<RgbModel> rgbs = buildAntiAliasedSpheres(numberOfRows, numberOfColumns, numberOfThreads);
    stopwatch.stop();

    LOG.info("Computed {} pixels with {} threads in {} ms", rgbs.size(), numberOfThreads, stopwatch.elapsed(TimeUnit.MILLISECONDS));

    BufferedImage bufferedImage = new BufferedImage(numberOfRows, numberOfColumns, BufferedImage.TYPE_3BYTE_BGR);
    for (RgbModel rgb : rgbs) {
      bufferedImage.setRGB(rgb.getCoordinates().getX(), rgb.getCoordinates().getY(), rgb.getColor().getRGB());
    }

    ImageIO.write(bufferedImage, "png", new File(String.format("%d-%s", numberOfThreads, FILENAME)));
  }

  private static List<RgbModel> buildAntiAliasedSpheres(int numberOfRows, int numberOfColumns, int numberOfThreads) {
    int numberOfSamples = 100;

    Hittable sphere1 = new Sphere(new Vector3(0, 0, -1), 0.5, new LambertianMaterial(new Vector3(0.1, 0.2, 0.5)));
    Hittable sphere2 = new Sphere(new Vector3(0, -100.5, -1), 100, new LambertianMaterial(new Vector3(0.8, 0.8, 0.0)));
    Hittable sphere3 = new Sphere(new Vector3(1, 0, -1), 0.5, new MetalMaterial(new Vector3(0.8, 0.6, 0.2), 0.3));
    Hittable sphere4 = new Sphere(new Vector3(-1, 0, -1), 0.5, new DielectricMaterial(1.5));
    Hittable sphere5 = new Sphere(new Vector3(-1, 0, -1), -0.45, new DielectricMaterial(1.5));

    HittablesList world = new HittablesList(ImmutableList.of(sphere1, sphere2, sphere3, sphere4, sphere5));

    Vector3 lookFrom = new Vector3(3, 3, 2);
    Vector3 lookAt = new Vector3(0, 0, -1);
    Vector3 viewUp = new Vector3(0, 1, 0);
    double distanceToFocus = lookFrom.subtract(lookAt).getNorm();
    double aperture = 2;
    double aspectRatio = (double) numberOfRows / (double) numberOfColumns;
    Camera camera = new Camera(lookFrom, lookAt, viewUp, 20, aspectRatio, aperture, distanceToFocus);

    ExecutorService executorService = buildExecutor(numberOfThreads);

    List<CompletableFuture<RgbModel>> futures = new ArrayList<>();
    for (int j = numberOfColumns; j > 0; j--) {
      for (int i = 0; i < numberOfRows; i++) {
        int finalI = i;
        int finalJ = j;

        CompletableFuture<RgbModel> future = CompletableFuture.supplyAsync(
            () -> buildRgb(numberOfSamples, numberOfRows, numberOfColumns, camera, world, finalI, finalJ),
            executorService
        );
        futures.add(future);
      }
    }

    List<RgbModel> rgbs = futures.stream()
        .map(CompletableFuture::join)
        .collect(ImmutableList.toImmutableList());

    executorService.shutdown();

    return rgbs;
  }

  private static RgbModel buildRgb(int numberOfSamples,
                                   int numberOfRows,
                                   int numberOfColumns,
                                   Camera camera,
                                   Hittable world,
                                   int i,
                                   int j) {
    Vector3 color = new Vector3(0, 0, 0);

    for (int s = 0; s < numberOfSamples; s++) {
      double u = ((double) (i + rand()) / numberOfRows);
      double v = ((double) (j + rand()) / numberOfColumns);

      Ray ray = camera.getRay(u, v);

      color = color.add(color(ray, world, 0));
    }

    color = color.scalarDivide(numberOfSamples);
    color = color.apply(Math::sqrt);

    int red = (int) (255.99 * color.getRed());
    int green = (int) (255.99 * color.getGreen());
    int blue = (int) (255.99 * color.getBlue());

    Coordinates coordinates = Coordinates.builder()
        .setX(i)
        .setY(numberOfColumns - j)
        .build();

    return Rgb.builder()
        .setCoordinates(coordinates)
        .setRed(red)
        .setGreen(green)
        .setBlue(blue)
        .build();
  }

  private static Vector3 color(Ray ray, Hittable hittable, int depth) {
    Optional<HitRecord> maybeHitRecord = hittable.hit(ray, 0.001, Double.MAX_VALUE);
    if (maybeHitRecord.isPresent()) {
      MaterialScatterRecord materialScatterRecord = maybeHitRecord.get()
          .getMaterial()
          .scatter(ray, maybeHitRecord.get());

      if (depth < 50 && materialScatterRecord.wasScattered()) {
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

  private static double rand() {
    return ThreadLocalRandom.current().nextDouble();
  }

  private static ExecutorService buildExecutor(int numberOfThreads) {
    return Executors.newFixedThreadPool(
        numberOfThreads,
        new ThreadFactoryBuilder()
            .setNameFormat("ray-tracer-%s")
            .build()
    );
  }

}
