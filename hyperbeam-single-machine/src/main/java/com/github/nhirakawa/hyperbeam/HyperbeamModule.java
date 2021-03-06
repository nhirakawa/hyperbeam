package com.github.nhirakawa.hyperbeam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.github.nhirakawa.hyperbeam.config.ConfigWrapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
class HyperbeamModule {

  @Provides
  @Singleton
  static ObjectMapper provideObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new GuavaModule());
    return objectMapper;
  }

  @Provides
  @Singleton
  static ConfigWrapper provideConfigWrapper() {
    Config config = ConfigFactory.load();

    return ConfigWrapper.builder().setConfig(config).build();
  }

  @Provides
  @Singleton
  static RayProcessor provideRayProcessor() {
    return new RayProcessor();
  }

  @Provides
  @Singleton
  static SortedHittablesFactory provideSortedHittablesFactory(
    RayProcessor rayProcessor
  ) {
    return new SortedHittablesFactory(rayProcessor);
  }

  @Provides
  @Singleton
  static RayTracer provideRayTracer(
    ObjectMapper objectMapper,
    ConfigWrapper configWrapper,
    RayProcessor rayProcessor,
    SortedHittablesFactory sortedHittablesFactory
  ) {
    return new RayTracer(
      objectMapper,
      configWrapper,
      rayProcessor,
      sortedHittablesFactory
    );
  }
}
