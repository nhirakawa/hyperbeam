package com.github.nhirakawa.hyperbeam.hadoop;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import com.github.nhirakawa.hyperbeam.hadoop.writables.RgbWritable;
import com.github.nhirakawa.hyperbeam.util.ObjectMapperInstance;

public class HyperbeamReducer implements Reducer<RgbWritable, NullWritable, Text, NullWritable> {

  private final Text text = new Text();

  @Override
  public void reduce(RgbWritable key,
                     Iterator<NullWritable> values,
                     OutputCollector<Text, NullWritable> outputCollector,
                     Reporter reporter) throws IOException {
    String rgbString = ObjectMapperInstance.instance().writeValueAsString(key.toRgb());
    text.set(rgbString);
    outputCollector.collect(text, NullWritable.get());
  }

  @Override
  public void close() throws IOException {

  }

  @Override
  public void configure(JobConf jobConf) {

  }

}
