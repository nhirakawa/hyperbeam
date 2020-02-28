package com.github.nhirakawa.hyperbeam;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = HyperbeamModule.class)
public interface RayTracerRunnerComponent {
  RayTracerRunner buildRayTracerRunner();
}
