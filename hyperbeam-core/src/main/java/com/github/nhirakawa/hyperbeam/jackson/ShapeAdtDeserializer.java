package com.github.nhirakawa.hyperbeam.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.nhirakawa.hyperbeam.shape.BoundingVolumeHierarchy;
import com.github.nhirakawa.hyperbeam.shape.Box;
import com.github.nhirakawa.hyperbeam.shape.ConstantMedium;
import com.github.nhirakawa.hyperbeam.shape.MovingSphere;
import com.github.nhirakawa.hyperbeam.shape.ReverseNormals;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectType;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectsList;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdt;
import com.github.nhirakawa.hyperbeam.shape.ShapeAdts;
import com.github.nhirakawa.hyperbeam.shape.Sphere;
import com.github.nhirakawa.hyperbeam.shape.XYRectangle;
import com.github.nhirakawa.hyperbeam.shape.XZRectangle;
import com.github.nhirakawa.hyperbeam.shape.YZRectangle;

public class ShapeAdtDeserializer extends StdDeserializer<ShapeAdt> {

  protected ShapeAdtDeserializer() {
    super(ShapeAdt.class);
  }

  @Override
  public ShapeAdt deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    ObjectNode objectNode = parser.readValueAsTree();

    SceneObjectType sceneObjectType = SceneObjectType.valueOf(objectNode.get("shapeType").asText());

    switch(sceneObjectType) {
      case BOUNDING_VOLUME_HIERARCHY:
        BoundingVolumeHierarchy boundingVolumeHierarchy = parser.getCodec().treeToValue(objectNode, BoundingVolumeHierarchy.class);
        return ShapeAdts.BOUNDING_VOLUME_HIERARCHY(boundingVolumeHierarchy);
      case BOX:
        Box box = parser.getCodec().treeToValue(objectNode, Box.class);
        return ShapeAdts.BOX(box);
      case CONSTANT_MEDIUM:
        ConstantMedium constantMedium = parser.getCodec().treeToValue(objectNode, ConstantMedium.class);
        return ShapeAdts.CONSTANT_MEDIUM(constantMedium);
      case MOVING_SPHERE:
        MovingSphere movingSphere = parser.getCodec().treeToValue(objectNode, MovingSphere.class);
        return ShapeAdts.MOVING_SPHERE(movingSphere);
      case REVERSE_NORMALS:
        ReverseNormals reverseNormals = parser.getCodec().treeToValue(objectNode, ReverseNormals.class);
        return ShapeAdts.REVERSE_NORMALS(reverseNormals);
      case SCENE_OBJECTS_LIST:
        SceneObjectsList sceneObjectsList = parser.getCodec().treeToValue(objectNode, SceneObjectsList.class);
        return ShapeAdts.SCENE_OBJECTS_LIST(sceneObjectsList);
      case SPHERE:
        Sphere sphere = parser.getCodec().treeToValue(objectNode, Sphere.class);
        return ShapeAdts.SPHERE(sphere);
      case XY_RECTANGLE:
        XYRectangle xyRectangle = parser.getCodec().treeToValue(objectNode, XYRectangle.class);
        return ShapeAdts.XY_RECTANGLE(xyRectangle);
      case XZ_RECTANGLE:
        XZRectangle xzRectangle = parser.getCodec().treeToValue(objectNode, XZRectangle.class);
        return ShapeAdts.XZ_RECTANGLE(xzRectangle);
      case YZ_RECTANGLE:
        YZRectangle yzRectangle = parser.getCodec().treeToValue(objectNode, YZRectangle.class);
        return ShapeAdts.YZ_RECTANGLE(yzRectangle);

      default: // TODO all scene obejcts need to be a scene object ADT
        throw new IllegalArgumentException(sceneObjectType + " cannot be deserialized");
    }
  }

}
