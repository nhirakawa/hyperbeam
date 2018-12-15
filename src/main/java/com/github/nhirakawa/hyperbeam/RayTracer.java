package com.github.nhirakawa.hyperbeam;

import static com.github.nhirakawa.hyperbeam.util.MathUtils.rand;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Timer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.github.nhirakawa.hyperbeam.camera.Camera;
import com.github.nhirakawa.hyperbeam.color.Rgb;
import com.github.nhirakawa.hyperbeam.color.RgbModel;
import com.github.nhirakawa.hyperbeam.config.ConfigWrapper;
import com.github.nhirakawa.hyperbeam.geometry.Coordinates;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.MaterialScatterRecord;
import com.github.nhirakawa.hyperbeam.scene.Scene;
import com.github.nhirakawa.hyperbeam.scene.SceneGenerator;
import com.github.nhirakawa.hyperbeam.shape.BoundingVolumeHierarchy;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectsList;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class RayTracer {

  private static final Logger LOG = LoggerFactory.getLogger(RayTracer.class);

  private static final Function<Double, Double> CLAMP = d -> Math.min(d, 1);

  private final ObjectMapper objectMapper;
  private final ConfigWrapper configWrapper;

  @Inject
  public RayTracer(ObjectMapper objectMapper,
                   ConfigWrapper configWrapper) {
    this.objectMapper = objectMapper;
    this.configWrapper = configWrapper;
  }

  public void doThreadedRayTrace() throws IOException {
    Scene scene = SceneGenerator.generateSphereAndLight();

    LOG.debug("Scene is {} bytes", objectMapper.writeValueAsBytes(scene).length);

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

    SceneObject world = BoundingVolumeHierarchy.builder()
        .setSceneObjectsList(new SceneObjectsList(scene.getSceneObjects()))
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

    logMetrics();

    return rgbs;
  }

  private static void logMetrics() {
    logTimers();
  }

  private static void logTimers() {
    for (Entry<String, Timer> entry : Metrics.instance().getTimers().entrySet()) {
      LOG.info("{}-50th - {}", entry.getKey(), entry.getValue().getSnapshot().getMedian());
      LOG.info("{}-75th - {}", entry.getKey(), entry.getValue().getSnapshot().get75thPercentile());
      LOG.info("{}-99th - {}", entry.getKey(), entry.getValue().getSnapshot().get99thPercentile());
    }
  }

  private static RgbModel buildRgb(int numberOfSamples,
                                   int numberOfRows,
                                   int numberOfColumns,
                                   Camera camera,
                                   SceneObject world,
                                   int i,
                                   int j) {
    Vector3 color = new Vector3(0, 0, 0);

    for (int s = 0; s < numberOfSamples; s++) {
      double u = ((i + rand()) / numberOfRows);
      double v = ((j + rand()) / numberOfColumns);

      Ray ray = camera.getRay(u, v);

      try (Timer.Context ignored = Metrics.getTimer("color").time()) {
        color = color.add(color(ray, world, 0));
      }
    }

    color = color.scalarDivide(numberOfSamples);
    color = color.apply(Math::sqrt);
    color = color.apply(CLAMP);

    int red = (int) (255 * color.getRed());
    int green = (int) (255 * color.getGreen());
    int blue = (int) (255 * color.getBlue());

    Coordinates coordinates = Coordinates.builder()
        .setX(i)
        .setY(numberOfColumns - j)
        .build();

    try {
      return Rgb.builder()
          .setCoordinates(coordinates)
          .setRed(red)
          .setGreen(green)
          .setBlue(blue)
          .build();
    } catch (IllegalArgumentException e) {
      throw e;
    }
  }

  private static Vector3 color(Ray ray, SceneObject sceneObject, int depth) {
    Optional<HitRecord> maybeHitRecord = sceneObject.hit(ray, 0.001, Double.MAX_VALUE);
    if (maybeHitRecord.isPresent()) {
      HitRecord hitRecord = maybeHitRecord.get();

      Vector3 emitted = hitRecord
          .getMaterial()
          .emit(hitRecord.getU(), hitRecord.getV(), hitRecord.getPoint());

      MaterialScatterRecord materialScatterRecord = hitRecord.getMaterial().scatter(ray, hitRecord);

      if (depth < 50 && materialScatterRecord.wasScattered()) {
        Vector3 addedColor = color(materialScatterRecord.getScattered(), sceneObject, depth + 1);
        return emitted.add(addedColor.multiply(materialScatterRecord.getAttenuation()));
      } else {
        return emitted;
      }
    } else {
      return Vector3.zero();
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
