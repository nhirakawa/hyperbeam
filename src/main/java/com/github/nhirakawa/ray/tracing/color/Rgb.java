package com.github.nhirakawa.ray.tracing.color;

import java.awt.*;

import com.github.nhirakawa.ray.tracing.main.Coordinates;

public class Rgb {

  private final Coordinates coordinates;
  private final int red;
  private final int green;
  private final int blue;
  private final Color color;

  public Rgb(Coordinates coordinates,
             int red,
             int green,
             int blue) {
    this.coordinates = coordinates;
    this.red = red;
    this.green = green;
    this.blue = blue;
    this.color = new Color(red, green, blue);
  }

  public Coordinates getCoordinates() {
    return coordinates;
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

  public Color getColor() {
    return color;
  }

}
