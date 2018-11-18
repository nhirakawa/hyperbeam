package com.github.nhirakawa.ray.tracing.material;

import org.immutables.value.Value;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;

@Value.Immutable
@ImmutableStyle
public interface MaterialScatterRecordModel {

  Vector3 getAttenuation();
  Ray getScattered();
  boolean wasScattered();

}
