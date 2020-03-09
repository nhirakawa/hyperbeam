package com.github.nhirakawa.hyperbeam.transform;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.RayProcessor;
import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectType;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import java.util.Optional;
import org.immutables.value.Value;

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

  public AxisAlignedBoundingBox getOffsetAxisAlignedBoundingBox(
    AxisAlignedBoundingBox axisAlignedBoundingBox
  ) {
    Vector3 offsetMin = axisAlignedBoundingBox.getMin().add(getOffset());
    Vector3 offsetMax = axisAlignedBoundingBox.getMax().add(getOffset());

    return AxisAlignedBoundingBox
      .builder()
      .setMin(offsetMin)
      .setMax(offsetMax)
      .build();
  }

  @Override
  public Optional<HitRecord> hit(
    RayProcessor rayProcessor,
    Ray ray,
    double tMin,
    double tMax
  ) {
    return rayProcessor.hitTranslation(this, ray, tMin, tMax);
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(
    RayProcessor rayProcessor,
    double t0,
    double t1
  ) {
    return rayProcessor.getBoundingBoxForTranslation(this, t0, t1);
  }
}
