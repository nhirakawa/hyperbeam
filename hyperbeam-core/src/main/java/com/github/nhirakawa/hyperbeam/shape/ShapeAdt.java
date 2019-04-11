package com.github.nhirakawa.hyperbeam.shape;

import org.derive4j.Data;

@Data
public abstract class ShapeAdt {

  interface Cases<R>{
    R BOX(Box box);
  }

  public abstract <R> R match(Cases<R> cases);
}
