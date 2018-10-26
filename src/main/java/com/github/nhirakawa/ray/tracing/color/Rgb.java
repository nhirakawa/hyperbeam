package com.github.nhirakawa.ray.tracing.color;

public class Rgb {

  private final int red;
  private final int green;
  private final int blue;

  public Rgb(int red,
             int green,
             int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  public int getRed() {
    return red;
  }

  public int getGreen() {
    return green;
  }

  public int getBlue() {
    return blue;
  }
}
