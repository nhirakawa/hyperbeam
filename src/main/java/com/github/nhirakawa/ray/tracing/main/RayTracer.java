package com.github.nhirakawa.ray.tracing.main;

import static com.github.nhirakawa.ray.tracing.util.MathUtils.rand;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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
import com.github.nhirakawa.ray.tracing.material.Material;
import com.github.nhirakawa.ray.tracing.material.MaterialScatterRecord;
import com.github.nhirakawa.ray.tracing.material.MetalMaterial;
import com.github.nhirakawa.ray.tracing.shape.HitRecord;
import com.github.nhirakawa.ray.tracing.shape.Hittable;
import com.github.nhirakawa.ray.tracing.shape.HittablesList;
import com.github.nhirakawa.ray.tracing.shape.MovingSphere;
import com.github.nhirakawa.ray.tracing.shape.Sphere;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class RayTracer {

  private static final Logger LOG = LoggerFactory.getLogger(RayTracer.class);

  private static final String FILENAME = "test.png";

  private static final int MULTIPLIER = 1;

  public static void main(String... args) throws Exception {
    int numberOfRows = 200 * MULTIPLIER;
    int numberOfColumns = 100 * MULTIPLIER;

    doThreadedRayTrace(numberOfRows, numberOfColumns, Runtime.getRuntime().availableProcessors());
  }

  private static void doThreadedRayTrace(int numberOfRows,
                                         int numberOfColumns,
                                         int numberOfThreads) throws IOException {
    Stopwatch stopwatch = Stopwatch.createStarted();
    List<RgbModel> rgbs = render(numberOfRows, numberOfColumns, numberOfThreads, buildRandomSpheres(50000));
    stopwatch.stop();

    LOG.info("Computed {} pixels with {} threads in {} ms", rgbs.size(), numberOfThreads, stopwatch.elapsed(TimeUnit.MILLISECONDS));

    BufferedImage bufferedImage = new BufferedImage(numberOfRows, numberOfColumns, BufferedImage.TYPE_3BYTE_BGR);
    for (RgbModel rgb : rgbs) {
      bufferedImage.setRGB(rgb.getCoordinates().getX(), rgb.getCoordinates().getY(), rgb.getColor().getRGB());
    }

    ImageIO.write(bufferedImage, "png", new File(String.format("%d-%s", numberOfThreads, FILENAME)));
  }

  private static List<RgbModel> render(int numberOfRows,
                                       int numberOfColumns,
                                       int numberOfThreads,
                                       HittablesList world) {
    int numberOfSamples = 100;

    Vector3 lookFrom = new Vector3(13, 2, 3);
    Vector3 lookAt = new Vector3(0, 0, 0);
    Vector3 viewUp = new Vector3(0, 1, 0);
    double distanceToFocus = 10;
    double aperture = 0;
    double aspectRatio = (double) numberOfRows / (double) numberOfColumns;
    Camera camera = Camera.builder()
        .setLookFrom(lookFrom)
        .setLookAt(lookAt)
        .setViewUp(viewUp)
        .setVerticalFovDegrees(20)
        .setAspectRatio(aspectRatio)
        .setAperture(aperture)
        .setFocusDistance(distanceToFocus)
        .setTime0(0)
        .setTime1(0)
        .build();

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

  private static ExecutorService buildExecutor(int numberOfThreads) {
    ExecutorService executorService = Executors.newFixedThreadPool(
        numberOfThreads,
        new ThreadFactoryBuilder()
            .setNameFormat("ray-tracer-%s")
            .setUncaughtExceptionHandler((thread, e) -> LOG.error("Uncaught exception in thread {}", thread.getName(), e))
            .build()
    );

    Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdown));

    return executorService;
  }

  private static HittablesList buildRandomSpheres(int numberOfSpheres) {
    List<Hittable> hittables = new ArrayList<>(numberOfSpheres);

    hittables.add(new Sphere(new Vector3(0, -1000, 0), 1000, new LambertianMaterial(new Vector3(0.5, 0.5, 0.5))));

    Set<Integer> bounds = IntStream.range(-10, 10)
        .boxed()
        .collect(ImmutableSet.toImmutableSet());

    Set<List<Integer>> product = Sets.cartesianProduct(bounds, bounds);

    for (List<Integer> pairs : product) {
      Preconditions.checkState(
          pairs.size() == 2,
          "Expected exactly 2 elements but found %s",
          pairs
      );

      int a = pairs.get(0);
      int b = pairs.get(1);

      Vector3 center = new Vector3(a + (0.9 * rand()), 0.2, b + (0.9 * rand()));
      Material material = getRandomMaterial();

      if (material instanceof LambertianMaterial) {
        hittables.add(
            MovingSphere.builder()
            .setCenter0(center)
            .setCenter1(center.add(new Vector3(0, 0.5 * rand(), 0)))
            .setTime0(0)
            .setTime1(1)
            .setRadius(0.2)
            .setMaterial(material)
            .build()
        );
      } else {
        hittables.add(new Sphere(center, 0.2, material));
      }
    }

    hittables.add(new Sphere(new Vector3(0, 1, 0), 1, new DielectricMaterial(1.5)));
    hittables.add(new Sphere(new Vector3(-4, 1, 0), 1, new LambertianMaterial(new Vector3(0.4, 0.2, 0.1))));
    hittables.add(new Sphere(new Vector3(4, 1, 0), 1, new MetalMaterial(new Vector3(0.7, 0.6, 0.5), 0)));

    return new HittablesList(hittables);
  }

  private static Material getRandomMaterial() {
    double chooseMaterial = rand();
    if (chooseMaterial < 0.8) {
      return new LambertianMaterial(new Vector3(rand() * rand(), rand() * rand(), rand() * rand()));
    } else if (chooseMaterial < 0.95) {
      Supplier<Double> random = () -> 0.5 * (1 + rand());
      return new MetalMaterial(new Vector3(random.get(), random.get(), random.get()), 0.5 * rand());
    } else {
      return new DielectricMaterial(1.5);
    }
  }

}
