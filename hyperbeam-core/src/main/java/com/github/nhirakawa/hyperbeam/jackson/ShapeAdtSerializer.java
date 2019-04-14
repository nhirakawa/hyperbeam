package com.github.nhirakawa.hyperbeam.jackson;

import java.io.IOException;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdt;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdts;

public class ShapeAdtSerializer extends StdSerializer<ShapeAdt> {

  protected ShapeAdtSerializer() {
    super(ShapeAdt.class);
  }

  @Override
  public void serialize(ShapeAdt shapeAdt, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
    ShapeAdts.caseOf(shapeAdt)
        .BOUNDING_VOLUME_HIERARCHY(this::forObject)
        .BOX(this::forObject)
        .CONSTANT_MEDIUM(this::forObject)
        .MOVING_SPHERE(this::forObject)
        .REVERSE_NORMALS(this::forObject)
        .SCENE_OBJECTS_LIST(this::forObject)
        .SPHERE(this::forObject)
        .TRANSLATION(this::forObject)
        .XY_RECTANGLE(this::forObject)
        .XZ_RECTANGLE(this::forObject)
        .YZ_RECTANGLE(this::forObject)
        .Y_ROTATION(this::forObject)
        .accept(jsonGenerator);
  }

  private Consumer<JsonGenerator> forObject(Object object) {
    return generator -> {
      try {
        generator.writeObject(object);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    };
  }
}
