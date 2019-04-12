package com.github.nhirakawa.hyperbeam.serde;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.nhirakawa.hyperbeam.shape.BoxModel;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdt;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdts;

public class ShapeSerializer extends StdSerializer<ShapeAdt> {

  protected ShapeSerializer() {
    super(ShapeAdt.class);
  }

  @Override
  public void serialize(ShapeAdt value, JsonGenerator generator, SerializerProvider provider) throws IOException {
    Optional<Consumer<JsonGenerator>> maybeGeneratorConsumer = ShapeAdts.caseOf(value)
        .BOX(this::forBox)
        .otherwiseEmpty();

    maybeGeneratorConsumer.ifPresent(consumer -> consumer.accept(generator));
  }

  private Consumer<JsonGenerator> forBox(BoxModel box) {
    throw new UnsupportedOperationException();
  }

}
