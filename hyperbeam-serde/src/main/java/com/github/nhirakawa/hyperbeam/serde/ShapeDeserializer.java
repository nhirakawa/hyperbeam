package com.github.nhirakawa.hyperbeam.serde;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdt;

public class ShapeDeserializer extends StdDeserializer<ShapeAdt> {

  protected ShapeDeserializer() {
    super(ShapeAdt.class);
  }

  @Override
  public ShapeAdt deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
    throw new UnsupportedOperationException();
  }

}
