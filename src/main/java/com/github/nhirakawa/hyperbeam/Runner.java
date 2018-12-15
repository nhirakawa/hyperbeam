package com.github.nhirakawa.hyperbeam;

import java.io.IOException;

import com.github.nhirakawa.hyperbeam.dagger.DaggerRayTracerComponent;

public class Runner {

  public static void main(String... args) throws IOException {
    DaggerRayTracerComponent.builder()
        .build()
        .buildRayTracer()
        .doThreadedRayTrace();
  }
}
