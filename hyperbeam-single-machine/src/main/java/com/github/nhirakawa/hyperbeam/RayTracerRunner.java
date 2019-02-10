package com.github.nhirakawa.hyperbeam;

import java.io.IOException;

import javax.inject.Inject;

public class RayTracerRunner {

  private final RayTracer rayTracer;

  @Inject
  RayTracerRunner(RayTracer rayTracer) {
    this.rayTracer = rayTracer;
  }

  void trace() throws IOException {
    rayTracer.doThreadedRayTrace();
  }

  public static void main(String... args) throws IOException {
    DaggerRayTracerRunnerComponent.builder()
        .build()
        .buildRayTracerRunner()
        .trace();
  }

}
