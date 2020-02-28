package com.github.nhirakawa.hyperbeam.material;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.texture.Texture;
import com.github.nhirakawa.hyperbeam.util.VectorUtils;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public abstract class IsotropicMaterialModel extends Material {

  public abstract Texture getTexture();

  @Override
  public MaterialType getMaterialType() {
    return MaterialType.ISOTROPIC;
  }

  @Override
  public MaterialScatterRecord scatter(Ray inRay, HitRecord hitRecord) {
    return MaterialScatterRecord
      .builder()
      .setScattered(
        Ray
          .builder()
          .setOrigin(hitRecord.getPoint())
          .setDirection(VectorUtils.getRandomUnitSphereVector())
          .setTime(0)
          .build()
      )
      .setAttenuation(
        getTexture()
          .getValue(hitRecord.getU(), hitRecord.getV(), hitRecord.getPoint())
      )
      .setWasScattered(true)
      .build();
  }
}
