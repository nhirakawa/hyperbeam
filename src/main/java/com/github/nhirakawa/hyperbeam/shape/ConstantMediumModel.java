package com.github.nhirakawa.hyperbeam.shape;

import static com.github.nhirakawa.hyperbeam.util.MathUtils.rand;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.IsotropicMaterial;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.hyperbeam.texture.Texture;

@Value.Immutable
@ImmutableStyle
public abstract class ConstantMediumModel implements SceneObject {

  public abstract SceneObject getSceneObject();
  public abstract double getDensity();
  public abstract Texture getTexture();

  @Value.Derived
  @JsonIgnore
  public Material getPhaseFunction() {
    return IsotropicMaterial.builder()
        .setTexture(getTexture())
        .build();
  }

  @Override
  public SceneObjectType getShapeType() {
    return SceneObjectType.CONSTANT_MEDIUM;
  }

  @Override
  public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
    Optional<HitRecord> hitRecord1 = getSceneObject().hit(ray, -Double.MAX_VALUE, Double.MAX_VALUE);
    if (!hitRecord1.isPresent()) {
      return Optional.empty();
    }

    Optional<HitRecord> hitRecord2 = getSceneObject().hit(ray, hitRecord1.get().getT() + 0.0001, Double.MAX_VALUE);
    if (!hitRecord2.isPresent()) {
      return Optional.empty();
    }

    double hitRecord1MaxT = hitRecord1.get().getT();
    if (hitRecord1MaxT < tMin) {
      hitRecord1MaxT = tMin;
    }

    double hitRecord2MinT = hitRecord2.get().getT();
    if (hitRecord2MinT > tMax) {
      hitRecord2MinT = tMax;
    }

    if (hitRecord1MaxT >= hitRecord2MinT) {
      return Optional.empty();
    }

    if (hitRecord1MaxT < 0) {
      hitRecord1MaxT = 0;
    }

    double distanceInsideBoundary = (hitRecord2MinT - hitRecord1MaxT) * ray.getDirection().getNorm();
    double hitDistance = (-1 / getDensity()) * Math.log(rand());

    if (hitDistance >= distanceInsideBoundary) {
      return Optional.empty();
    }

    double t = hitRecord1MaxT + (hitDistance / ray.getDirection().getNorm());
    Vector3 point = ray.getPointAtParameter(t);

    return Optional.of(
        HitRecord.builder()
            .setPoint(point)
            .setMaterial(getPhaseFunction())
            .setNormal(new Vector3(1, 0, 0))
            .setT(t)
            .build()
    );

  }

  @Override
  public Optional<AxisAlignedBoundingBox> getBoundingBox(double t0, double t1) {
    return getSceneObject().getBoundingBox(t0, t1);
  }

}