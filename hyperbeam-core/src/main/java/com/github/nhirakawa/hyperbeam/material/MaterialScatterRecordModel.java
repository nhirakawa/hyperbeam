package com.github.nhirakawa.hyperbeam.material;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface MaterialScatterRecordModel {
  Vector3 getAttenuation();
  Ray getScattered();
  boolean wasScattered();
}
