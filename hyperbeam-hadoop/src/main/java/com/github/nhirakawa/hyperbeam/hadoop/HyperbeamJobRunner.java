package com.github.nhirakawa.hyperbeam.hadoop;

import java.io.IOException;
import java.time.Instant;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileAsBinaryInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nhirakawa.hyperbeam.hadoop.writables.HyperbeamKeyWritable;
import com.github.nhirakawa.hyperbeam.hadoop.writables.RgbWritable;
import com.github.nhirakawa.hyperbeam.scene.Scene;
import com.github.nhirakawa.hyperbeam.scene.SceneGenerator;
import com.github.nhirakawa.hyperbeam.util.ObjectMapperInstance;
import com.google.common.base.Preconditions;

public class HyperbeamJobRunner {

  private static final Logger LOG = LoggerFactory.getLogger(HyperbeamJobRunner.class);

  private final Instant start;

  public HyperbeamJobRunner() {
    this.start = Instant.now();
  }

  public void run() throws Exception {
    createAndWriteInputFiles();

    JobConf jobConf = buildJobConf();
    Job job = Job.getInstance(jobConf, "hyperbeam-" + start.toEpochMilli());

    boolean isOk = job.waitForCompletion(false);
    Preconditions.checkState(isOk, "Job did not complete successfully");
  }

  private JobConf buildJobConf() {
    Configuration conf = new Configuration();
    JobConf jobConf = new JobConf(conf, HyperbeamJob.class);

    jobConf.setJarByClass(HyperbeamJob.class);

    jobConf.setMapperClass(HyperbeamMapper.class);

    jobConf.setMapOutputKeyClass(NullWritable.class);
    jobConf.setMapOutputValueClass(RgbWritable.class);

    jobConf.setReducerClass(HyperbeamReducer.class);
    jobConf.setNumReduceTasks(10);
    jobConf.setOutputKeyClass(RgbWritable.class);
    jobConf.setOutputValueClass(NullWritable.class);

    SequenceFileAsBinaryInputFormat.addInputPath(jobConf, buildInputPath());
    FileOutputFormat.setOutputPath(jobConf, buildOutputPath());

    return jobConf;
  }

  private Path buildInputPath() {
    return new Path("hadoop/" + start.toEpochMilli() + "/input");
  }

  private Path buildOutputPath() {
    return new Path("hadoop/" + start.toEpochMilli() + "/output");
  }

  private void createAndWriteInputFiles() throws IOException {
    Scene scene = SceneGenerator.generateCornellBox();

    BytesWritable bytesWritable = new BytesWritable(ObjectMapperInstance.instance().writeValueAsBytes(scene));

    SequenceFile.Writer writer = buildWriter(buildInputFilePath());

    for (int i = 0; i < scene.getOutput().getNumberOfRows(); i++) {
      for (int j = 0; j < scene.getOutput().getNumberOfColumns(); j++) {
        HyperbeamKeyWritable key = new HyperbeamKeyWritable(i, j);

        writer.append(key, bytesWritable);
      }
    }
  }

  private Path buildInputFilePath() {
    return new Path(String.format("hadoop/%s/input/scene", start.toEpochMilli()));
  }

  private Path buildInputFilePath(int i, int j) {
    String filename = "hadoop/" + start.toEpochMilli() + "/input/" + i + "-" + j;
    return new Path(filename);
  }

  private static SequenceFile.Writer buildWriter(Path path) throws IOException {
    return SequenceFile.createWriter(
        new Configuration(),
        SequenceFile.Writer.keyClass(HyperbeamKeyWritable.class),
        SequenceFile.Writer.valueClass(BytesWritable.class),
        SequenceFile.Writer.file(path),
        SequenceFile.Writer.compression(CompressionType.NONE)
    );
  }

  public static void main(String... args) throws Exception {
    new HyperbeamJobRunner().run();
  }

}
