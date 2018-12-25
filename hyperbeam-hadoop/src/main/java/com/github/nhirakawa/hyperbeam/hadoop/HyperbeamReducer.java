package com.github.nhirakawa.hyperbeam.hadoop;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import com.github.nhirakawa.hyperbeam.hadoop.writables.RgbWritable;

public class HyperbeamReducer implements Reducer<RgbWritable, NullWritable, RgbWritable, NullWritable> {

  @Override
  public void reduce(RgbWritable key,
                     Iterator<NullWritable> values,
                     OutputCollector<RgbWritable, NullWritable> outputCollector,
                     Reporter reporter) throws IOException {
    outputCollector.collect(key, NullWritable.get());
  }

  @Override
  public void close() throws IOException {

  }

  @Override
  public void configure(JobConf jobConf) {

  }

}
