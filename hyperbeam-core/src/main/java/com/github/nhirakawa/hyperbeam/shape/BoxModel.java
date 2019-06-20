package com.github.nhirakawa.hyperbeam.shape;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.hyperbeam.AlgebraicSceneObject;
import com.github.nhirakawa.hyperbeam.AlgebraicSceneObjects;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.material.Material;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.google.common.collect.ImmutableList;

@Value.Immutable
@ImmutableStyle
public abstract class BoxModel implements SceneObject {

  public abstract Vector3 getPMin();
  public abstract Vector3 getPMax();
  public abstract Material getMaterial();

  @Override
  @JsonIgnore
  @Value.Lazy
  public AlgebraicSceneObject toAlgebraicSceneObject() {
    return AlgebraicSceneObjects.BOX(this);
  }

  @Value.Lazy
  @JsonIgnore
  public SceneObjectsList getSceneObjectsList() {
    return new SceneObjectsList(
        ImmutableList.of(
            AlgebraicSceneObjects.XY_RECTANGLE(
                XYRectangle.builder() // 0
                    .setX0(getPMin().getX())
                    .setX1(getPMax().getX())
                    .setY0(getPMin().getY())
                    .setY1(getPMax().getY())
                    .setK(getPMax().getZ())
                    .setMaterial(getMaterial())
                    .build()
            ),
            AlgebraicSceneObjects.REVERSE_NORMALS(
                ReverseNormals.builder() // 1
                    .setSceneObject(
                        AlgebraicSceneObjects.XY_RECTANGLE(
                            XYRectangle.builder()
                                .setX0(getPMin().getX())
                                .setX1(getPMax().getX())
                                .setY0(getPMin().getY())
                                .setY1(getPMax().getY())
                                .setK(getPMin().getZ())
                                .setMaterial(getMaterial())
                                .build()
                        )
                    )
                    .build()
            ),
            AlgebraicSceneObjects.XZ_RECTANGLE(
                XZRectangle.builder() // 2
                    .setX0(getPMin().getX())
                    .setX1(getPMax().getX())
                    .setZ0(getPMin().getZ())
                    .setZ1(getPMax().getZ())
                    .setK(getPMax().getY())
                    .setMaterial(getMaterial())
                    .build()
            ),
            AlgebraicSceneObjects.REVERSE_NORMALS(
                ReverseNormals.builder() // 3
                    .setSceneObject(
                        AlgebraicSceneObjects.XZ_RECTANGLE(
                            XZRectangle.builder()
                                .setX0(getPMin().getX())
                                .setX1(getPMax().getX())
                                .setZ0(getPMin().getZ())
                                .setZ1(getPMax().getZ())
                                .setK(getPMin().getY())
                                .setMaterial(getMaterial())
                                .build()
                        )
                    )
                    .build()
            ),
            AlgebraicSceneObjects.YZ_RECTANGLE(
                YZRectangle.builder() // 4
                    .setY0(getPMin().getY())
                    .setY1(getPMax().getY())
                    .setZ0(getPMin().getZ())
                    .setZ1(getPMax().getZ())
                    .setK(getPMax().getX())
                    .setMaterial(getMaterial())
                    .build()
            ),
            AlgebraicSceneObjects.REVERSE_NORMALS(
                ReverseNormals.builder() // 5
                    .setSceneObject(
                        AlgebraicSceneObjects.YZ_RECTANGLE(
                            YZRectangle.builder()
                                .setY0(getPMin().getY())
                                .setY1(getPMax().getY())
                                .setZ0(getPMin().getZ())
                                .setZ1(getPMax().getZ())
                                .setK(getPMin().getX())
                                .setMaterial(getMaterial())
                                .build()
                        )
                    )
                    .build()
            )
        )
    );
  }

  @Value.Lazy
  @JsonIgnore
  public AxisAlignedBoundingBox getBoundingBox() {
    return AxisAlignedBoundingBox.builder()
        .setMin(getPMin())
        .setMax(getPMax())
        .build();
  }

  @Override
  @Value.Auxiliary
  public SceneObjectType getShapeType() {
    return SceneObjectType.BOX;
  }

}
