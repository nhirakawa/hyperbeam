package com.github.nhirakawa.ray.tracing.transform;

import java.util.Optional;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.ray.tracing.shape.HitRecord;
import com.github.nhirakawa.ray.tracing.shape.SceneObject;
import com.github.nhirakawa.ray.tracing.shape.SceneObjectType;

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
