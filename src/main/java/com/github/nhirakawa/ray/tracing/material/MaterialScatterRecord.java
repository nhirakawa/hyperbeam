package com.github.nhirakawa.ray.tracing.material;

import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;

public class MaterialScatterRecord {

  private final Vector3 attenuation;
  private final Ray scattered;
  private final boolean wasScattered;

  public MaterialScatterRecord(Vector3 attenuation,
                               Ray scattered,
                               boolean wasScattered) {
    this.attenuation = attenuation;
    this.scattered = scattered;
    this.wasScattered = wasScattered;
  }

  public Vector3 getAttenuation() {
    return attenuation;
  }

  public Ray getScattered() {
    return scattered;
  }

  public boolean isWasScattered() {
    return wasScattered;
  }
}
