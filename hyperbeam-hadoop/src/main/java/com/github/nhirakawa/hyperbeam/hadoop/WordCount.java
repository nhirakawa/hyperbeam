package com.github.nhirakawa.hyperbeam.hadoop;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Job;

public class WordCount {

  private static class TokenizerMapper implements Mapper<Object, Text, Text, IntWritable> {

    private static final IntWritable one = new IntWritable(1);
    private final Text word = new Text();

    @Override
    public void map(Object o,
                    Text text,
                    OutputCollector<Text, IntWritable> outputCollector,
                    Reporter reporter) throws IOException {
      StringTokenizer itr = new StringTokenizer(text.toString());

      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        outputCollector.collect(word, one);
      }
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void configure(JobConf jobConf) {

    }

  }

  private static class IntSumReducer implements Reducer<Text, IntWritable, Text, IntWritable> {

    private final IntWritable result = new IntWritable();

    @Override
    public void reduce(Text text,
                       Iterator<IntWritable> iterator,
                       OutputCollector<Text, IntWritable> outputCollector,
                       Reporter reporter) throws IOException {
      int sum = 0;

      while (iterator.hasNext()) {
        sum += iterator.next().get();
      }

      result.set(sum);

      outputCollector.collect(text, result);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void configure(JobConf jobConf) {

    }

  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();

    JobConf jobConf = new JobConf(conf, WordCount.class);

    jobConf.setJarByClass(WordCount.class);

    jobConf.setMapperClass(TokenizerMapper.class);
    jobConf.setCombinerClass(IntSumReducer.class);
    jobConf.setReducerClass(IntSumReducer.class);

    jobConf.setOutputKeyClass(Text.class);
    jobConf.setOutputValueClass(IntWritable.class);

    Path outputPath = new Path("output/hadoop-output-" + System.currentTimeMillis());

    FileInputFormat.addInputPath(jobConf, new Path("input"));
    FileOutputFormat.setOutputPath(jobConf, outputPath);

    Job job = Job.getInstance(jobConf, "word count");

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }

}
