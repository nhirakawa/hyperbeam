package com.github.nhirakawa.hyperbeam.dagger;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.github.nhirakawa.hyperbeam.config.ConfigWrapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import dagger.Module;
import dagger.Provides;

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

    return ConfigWrapper.builder()
        .setConfig(config)
        .build();
  }

}
