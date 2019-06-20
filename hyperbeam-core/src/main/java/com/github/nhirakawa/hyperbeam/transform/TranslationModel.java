package com.github.nhirakawa.hyperbeam.transform;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.AlgebraicSceneObject;
import com.github.nhirakawa.hyperbeam.AlgebraicSceneObjects;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectType;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public abstract class TranslationModel implements SceneObject {

  public abstract AlgebraicSceneObject getSceneObject();
  public abstract Vector3 getOffset();

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.TRANSLATION;
  }

  @Override
  @JsonIgnore
  @Value.Lazy
  public AlgebraicSceneObject toAlgebraicSceneObject() {
    return AlgebraicSceneObjects.TRANSLATION(this);
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
