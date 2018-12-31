package com.github.nhirakawa.hyperbeam.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ObjectMapperInstance {

  private static final ObjectMapper INSTANCE = build();

  private ObjectMapperInstance() {}

  public static ObjectMapper instance() {
    return INSTANCE;
  }

  private static ObjectMapper build() {
    ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.registerModule(new GuavaModule());
    return objectMapper;
  }

}
