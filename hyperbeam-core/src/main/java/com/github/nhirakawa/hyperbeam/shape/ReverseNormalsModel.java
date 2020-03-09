package com.github.nhirakawa.hyperbeam.shape;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.RayProcessor;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public abstract class ReverseNormalsModel implements SceneObject {

  public abstract SceneObject getSceneObject();

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.REVERSE_NORMALS;
  }

  @Override
  public Optional<HitRecord> hit(
    RayProcessor rayProcessor,
    Ray ray,
    double tMin,
    double tMax
  ) {
    return rayProcessor.hitReverseNormals(this, ray, tMin, tMax);
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(
    RayProcessor rayProcessor,
    double t0,
    double t1
  ) {
    return rayProcessor.getBoundingBoxForReverseNormals(this, t0, t1);
  }
}
