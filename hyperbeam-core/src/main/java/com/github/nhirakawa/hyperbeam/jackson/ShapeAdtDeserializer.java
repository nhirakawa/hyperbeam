package com.github.nhirakawa.hyperbeam.jackson;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectType;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdt;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdts;
import com.github.nhirakawa.hyperbeam.shape.Sphere;

public class ShapeAdtDeserializer extends StdDeserializer<ShapeAdt> {

  private static final Logger LOG = LoggerFactory.getLogger(ShapeAdtDeserializer.class);

  protected ShapeAdtDeserializer() {
    super(ShapeAdt.class);
  }

  @Override
  public ShapeAdt deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
    ObjectNode objectNode = parser.readValueAsTree();

    SceneObjectType sceneObjectType = SceneObjectType.valueOf(objectNode.get("shapeType").asText());

    switch(sceneObjectType) {
      case SPHERE:
        Sphere sphere = parser.getCodec().treeToValue(objectNode, Sphere.class);
        return ShapeAdts.SPHERE(sphere);
    }

    throw new UnsupportedOperationException();
  }

}
