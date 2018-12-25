package com.github.nhirakawa.hyperbeam.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

import com.google.common.collect.ComparisonChain;

public class HyperbeamKeyWritable implements WritableComparable<HyperbeamKeyWritable> {

  private int i;
  private int j;

  public HyperbeamKeyWritable(int i, int j) {
    this.i = i;
    this.j = j;
  }

  public int getI() {
    return i;
  }

  public int getJ() {
    return j;
  }

  @Override
  public int compareTo(HyperbeamKeyWritable other) {
    return ComparisonChain.start()
        .compare(i, other.i)
        .compare(j, other.j)
        .result();
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    dataOutput.writeInt(i);
    dataOutput.writeInt(j);
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    i = dataInput.readInt();
    j = dataInput.readInt();
  }

}
