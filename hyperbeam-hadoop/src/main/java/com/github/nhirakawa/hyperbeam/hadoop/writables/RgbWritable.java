package com.github.nhirakawa.hyperbeam.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

import com.github.nhirakawa.hyperbeam.color.Rgb;
import com.google.common.collect.ComparisonChain;

public class RgbWritable implements WritableComparable<RgbWritable> {

  private int x;
  private int y;
  private int red;
  private int green;
  private int blue;

  public RgbWritable(Rgb rgb) {
    this.x = rgb.getCoordinates().getX();
    this.y = rgb.getCoordinates().getY();
    this.red = rgb.getRed();
    this.green = rgb.getGreen();
    this.blue = rgb.getBlue();
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
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

  @Override
  public int compareTo(RgbWritable other) {
    return ComparisonChain.start()
        .compare(x, other.x)
        .compare(y, other.y)
        .compare(red, other.red)
        .compare(green, other.green)
        .compare(blue, other.blue)
        .result();
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    dataOutput.writeInt(x);
    dataOutput.writeInt(y);
    dataOutput.writeInt(red);
    dataOutput.writeInt(green);
    dataOutput.writeInt(blue);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    x = dataInput.readInt();
    y = dataInput.readInt();
    red = dataInput.readInt();
    green = dataInput.readInt();
    blue = dataInput.readInt();
  }

}
