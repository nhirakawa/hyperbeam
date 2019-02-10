package com.github.nhirakawa.hyperbeam;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = HyperbeamModule.class)
public interface RayTracerRunnerComponent {
  RayTracerRunner buildRayTracerRunner();
}
