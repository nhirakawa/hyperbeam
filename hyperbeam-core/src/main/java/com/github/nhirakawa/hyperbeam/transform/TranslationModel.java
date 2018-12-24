package com.github.nhirakawa.hyperbeam.transform;

import java.util.Optional;

import org.immutables.value.Value;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectType;
import com.github.nhirakawa.immutable.style.ImmutableStyle;

@Value.Immutable
@ImmutableStyle
public abstract class TranslationModel implements SceneObject {

  public abstract SceneObject getSceneObject();
  public abstract Vector3 getOffset();

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    Ray offsetRay = ray.withOrigin(ray.getOrigin().subtract(getOffset()));

    return getSceneObject().hit(offsetRay, tMin, tMax)
        .map(hitRecord -> hitRecord.withPoint(hitRecord.getPoint().add(getOffset())));
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return getSceneObject().getBoundingBox(t0, t1)
        .map(this::getOffsetAxisAlignedBoundingBox);
  }

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.TRANSLATION;
  }

  private AxisAlignedBoundingBox getOffsetAxisAlignedBoundingBox(AxisAlignedBoundingBox axisAlignedBoundingBox) {
    Vector3 offsetMin = axisAlignedBoundingBox.getMin().add(getOffset());
    Vector3 offsetMax = axisAlignedBoundingBox.getMax().add(getOffset());

    return AxisAlignedBoundingBox.builder()
        .setMin(offsetMin)
        .setMax(offsetMax)
        .build();
  }

}
