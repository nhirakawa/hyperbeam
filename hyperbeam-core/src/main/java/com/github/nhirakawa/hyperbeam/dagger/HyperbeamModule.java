package com.github.nhirakawa.hyperbeam.dagger;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nhirakawa.hyperbeam.config.ConfigWrapper;
import com.github.nhirakawa.hyperbeam.util.ObjectMapperInstance;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import dagger.Module;
import dagger.Provides;

@Module
class HyperbeamModule {

  @Provides
  @Singleton
  static ObjectMapper provideObjectMapper() {
    return ObjectMapperInstance.instance();
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
