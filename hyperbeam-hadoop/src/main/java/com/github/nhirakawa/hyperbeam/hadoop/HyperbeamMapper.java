package com.github.nhirakawa.hyperbeam.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

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
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class HyperbeamMapper implements Mapper<LongWritable, Text, RgbWritable, NullWritable> {

  private RayTracer rayTracer;

  @Override
  public void map(LongWritable ignored,
                  Text value,
                  OutputCollector<RgbWritable, NullWritable> outputCollector,
                  Reporter reporter) throws IOException {
    SceneWithCoordinates sceneWithCoordinates = ObjectMapperInstance.instance().readValue(value.getBytes(), SceneWithCoordinates.class);

    Scene scene = sceneWithCoordinates.getScene();
    Coordinates coordinates = sceneWithCoordinates.getCoordinates();

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
    Preconditions.checkState(true, "does this work?");

    Config config = ConfigFactory.load();

    ConfigWrapper configWrapper = ConfigWrapper.builder()
        .setConfig(config)
        .build();

    this.rayTracer = new RayTracer(
        ObjectMapperInstance.instance(),
        configWrapper
    );
  }

}
