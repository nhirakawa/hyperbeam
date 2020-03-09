package com.github.nhirakawa.hyperbeam.shape;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.hyperbeam.RayProcessor;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import java.util.Optional;
import org.immutables.value.internal.$processor$.meta.$OkTypeAdaptersMirror;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public abstract class YZRectangleModel implements SceneObject {
  private static final Vector3 NORMAL = Vector3
    .builder()
    .setX(1)
    .setY(0)
    .setZ(0)
    .build();

  public abstract double getY0();

  public abstract double getY1();

  public abstract double getZ0();

  public abstract double getZ1();

  public abstract double getK();

  public abstract Material getMaterial();

  @Value.Lazy
  @JsonIgnore
  public Vector3 getNormal() {
    return NORMAL;
  }

  @Value.Lazy
  @JsonIgnore
  public AxisAlignedBoundingBox getBoundingBox() {
    Vector3 min = Vector3
      .builder()
      .setX(getK() - 0.0001)
      .setY(getY0())
      .setZ(getZ0())
      .build();

    Vector3 max = Vector3
      .builder()
      .setX(getK() + 0.0001)
      .setY(getY1())
      .setZ(getZ1())
      .build();

    return AxisAlignedBoundingBox.builder().setMin(min).setMax(max).build();
  }

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.YZ_RECTANGLE;
  }

  @Override
  public Optional<HitRecord> hit(
    RayProcessor rayProcessor,
    Ray ray,
    double tMin,
    double tMax
  ) {
    return rayProcessor.hitYZRectangle(this, ray, tMin, tMax);
  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(
    RayProcessor rayProcessor,
    double t0,
    double t1
  ) {
    return rayProcessor.getBoundingBoxForYZRectangle(this, t0, t1);
  }
}
