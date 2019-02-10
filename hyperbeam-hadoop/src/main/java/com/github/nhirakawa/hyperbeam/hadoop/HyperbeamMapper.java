package com.github.nhirakawa.hyperbeam.hadoop;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.nhirakawa.hyperbeam.RayTracer;
import com.github.nhirakawa.hyperbeam.color.Rgb;
import com.github.nhirakawa.hyperbeam.config.ConfigWrapper;
import com.github.nhirakawa.hyperbeam.geometry.Coordinates;
import com.github.nhirakawa.hyperbeam.hadoop.writables.RgbWritable;
import com.github.nhirakawa.hyperbeam.scene.Scene;
import com.github.nhirakawa.hyperbeam.shape.BoundingVolumeHierarchy;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectsList;
import com.github.nhirakawa.hyperbeam.util.ObjectMapperInstance;
import com.google.common.base.Throwables;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class HyperbeamMapper implements Mapper<Text, Text, RgbWritable, NullWritable> {

  private static final Logger LOG = LoggerFactory.getLogger(HyperbeamMapper.class);

  private static final LoadingCache<String, Scene> SCENE_CACHE = Caffeine.newBuilder()
      .build(new Loader());

  private RayTracer rayTracer;

  @Override
  public void map(Text key, Text value, OutputCollector<RgbWritable, NullWritable> outputCollector, Reporter reporter) throws IOException {
    LOG.info("key: {}", key);

    Coordinates coordinates = ObjectMapperInstance.instance().readValue(key.getBytes(), Coordinates.class);
    Scene scene = SCENE_CACHE.get(value.toString());

    SceneObject world = BoundingVolumeHierarchy.builder()
        .setSceneObjectsList(new SceneObjectsList(scene.getSceneObjects()))
        .setTime0(0)
        .setTime1(1)
        .build();

    Rgb rgb = rayTracer.buildRgb(scene.getCamera(), world, scene.getOutput(), coordinates.getX(), coordinates.getY());

    outputCollector.collect(new RgbWritable(rgb), NullWritable.get());
  }

  @Override
  public void close() throws IOException {

  }

  @Override
  public void configure(JobConf jobConf) {
    Config config = ConfigFactory.load();

    ConfigWrapper configWrapper = ConfigWrapper.builder()
        .setConfig(config)
        .build();

    this.rayTracer = new RayTracer(
        ObjectMapperInstance.instance(),
        configWrapper
    );
  }

  private static class Loader implements CacheLoader<String, Scene> {

    private static final Logger LOG = LoggerFactory.getLogger(Loader.class);

    @Nullable
    @Override
    public Scene load(@Nonnull String key) throws Exception {
      try {
        LOG.info("Loading {}", key);
        File file = new File(key);
        return ObjectMapperInstance.instance().readValue(file, Scene.class);
      } catch (Exception e) {
        Throwables.throwIfUnchecked(e);
        throw new RuntimeException(e);
      }
    }

  }

}
