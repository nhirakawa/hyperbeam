package com.github.nhirakawa.hyperbeam.serde;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdt;

public class ShapeSerializer extends StdSerializer<ShapeAdt> {

  protected ShapeSerializer() {
    super(ShapeAdt.class);
  }

  @Override
  public void serialize(ShapeAdt value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    throw new UnsupportedOperationException();
  }

}
