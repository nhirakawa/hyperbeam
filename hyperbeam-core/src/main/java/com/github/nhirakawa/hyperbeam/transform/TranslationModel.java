package com.github.nhirakawa.hyperbeam.transform;

import org.immutables.value.Value;

import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectType;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public abstract class TranslationModel implements SceneObject {

  public abstract SceneObject getSceneObject();
  public abstract Vector3 getOffset();

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.TRANSLATION;
  }

  public AxisAlignedBoundingBox getOffsetAxisAlignedBoundingBox(AxisAlignedBoundingBox axisAlignedBoundingBox) {
    Vector3 offsetMin = axisAlignedBoundingBox.getMin().add(getOffset());
    Vector3 offsetMax = axisAlignedBoundingBox.getMax().add(getOffset());

    return AxisAlignedBoundingBox.builder()
        .setMin(offsetMin)
        .setMax(offsetMax)
        .build();
  }
}
