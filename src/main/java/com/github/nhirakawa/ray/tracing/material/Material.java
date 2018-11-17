package com.github.nhirakawa.ray.tracing.material;

import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.shape.HitRecord;

public interface Material {

  MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord);
}
