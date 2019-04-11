package com.github.nhirakawa.hyperbeam.shape;

import org.derive4j.Data;

@Data
public abstract class ShapeAdt {

  interface Cases<R> {

    R BOX(BoxModel box);
    R CONSTANT_MEDIUM(ConstantMediumModel constantMedium);
    R MOVING_SPHERE(MovingSphereModel movingSphere);
    R REVERSE_NORMALS(ReverseNormalsModel reverseNormals);

  }

  public abstract <R> R match(Cases<R> cases);

}
