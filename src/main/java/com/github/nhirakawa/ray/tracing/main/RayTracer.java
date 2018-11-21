package com.github.nhirakawa.ray.tracing.main;

import static com.github.nhirakawa.ray.tracing.util.MathUtils.rand;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.github.nhirakawa.ray.tracing.camera.Camera;
import com.github.nhirakawa.ray.tracing.collision.BoundingVolumeHierarchy;
import com.github.nhirakawa.ray.tracing.collision.HitRecord;
import com.github.nhirakawa.ray.tracing.collision.Hittable;
import com.github.nhirakawa.ray.tracing.collision.HittablesList;
import com.github.nhirakawa.ray.tracing.color.Rgb;
import com.github.nhirakawa.ray.tracing.color.RgbModel;
import com.github.nhirakawa.ray.tracing.config.ConfigWrapper;
import com.github.nhirakawa.ray.tracing.geometry.Coordinates;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.material.MaterialScatterRecord;
import com.github.nhirakawa.ray.tracing.scene.Scene;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class RayTracer {

  private static final Logger LOG = LoggerFactory.getLogger(RayTracer.class);

  private static final ObjectMapper OBJECT_MAPPER = buildObjectMapper();

  public static void main(String... args) throws Exception {
    Config config = ConfigFactory.load();
    ConfigWrapper configWrapper = ConfigWrapper.builder()
        .setConfig(config)
        .build();

    doThreadedRayTrace(configWrapper);
  }

  private static void doThreadedRayTrace(ConfigWrapper configWrapper) throws IOException {
    byte[] bytes = Resources.toByteArray(Resources.getResource("checkerboard-spheres.json"));
    Scene scene = OBJECT_MAPPER.readValue(bytes, Scene.class);

    Stopwatch stopwatch = Stopwatch.createStarted();
    List<RgbModel> rgbs = render(configWrapper, scene);
    stopwatch.stop();

    LOG.info("Computed {} pixels with {} threads in {} ms", rgbs.size(), configWrapper.getNumberOfThreads(), stopwatch.elapsed(TimeUnit.MILLISECONDS));

    BufferedImage bufferedImage = new BufferedImage(configWrapper.getNumberOfRows(), configWrapper.getNumberOfColumns(), BufferedImage.TYPE_3BYTE_BGR);
    for (RgbModel rgb : rgbs) {
      bufferedImage.setRGB(rgb.getCoordinates().getX(), rgb.getCoordinates().getY(), rgb.getColor().getRGB());
    }

    ImageIO.write(bufferedImage, "png", new File(configWrapper.getOutFile()));
  }

  private static List<RgbModel> render(ConfigWrapper configWrapper, Scene scene) {
    ExecutorService executorService = buildExecutor(configWrapper.getNumberOfThreads());

    Hittable world = BoundingVolumeHierarchy.builder()
        .setHittablesList(new HittablesList(scene.getShapes()))
        .setTime0(0)
        .setTime1(1)
        .build();

    List<CompletableFuture<RgbModel>> futures = new ArrayList<>();
    for (int j = configWrapper.getNumberOfColumns(); j > 0; j--) {
      for (int i = 0; i < configWrapper.getNumberOfRows(); i++) {
        int finalI = i;
        int finalJ = j;

        CompletableFuture<RgbModel> future = CompletableFuture.supplyAsync(
            () -> buildRgb(
                configWrapper.getNumberOfSamples(),
                configWrapper.getNumberOfRows(),
                configWrapper.getNumberOfColumns(),
                scene.getCamera(),
                world,
                finalI,
                finalJ
            ),
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

  private static ObjectMapper buildObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new GuavaModule());
    return objectMapper;
  }

}
