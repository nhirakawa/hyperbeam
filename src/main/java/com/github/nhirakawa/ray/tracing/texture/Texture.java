package com.github.nhirakawa.ray.tracing.texture;

import com.github.nhirakawa.ray.tracing.geometry.Vector3;

public interface Texture {

  Vector3 getValue(double u, double v, Vector3 point);
}
