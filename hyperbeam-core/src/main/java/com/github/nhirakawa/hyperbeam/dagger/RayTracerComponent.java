package com.github.nhirakawa.hyperbeam.dagger;

import javax.inject.Singleton;

import com.github.nhirakawa.hyperbeam.RayTracer;

import dagger.Component;

@Singleton
@Component(modules = HyperbeamModule.class)
public interface RayTracerComponent {
  RayTracer buildRayTracer();
}
