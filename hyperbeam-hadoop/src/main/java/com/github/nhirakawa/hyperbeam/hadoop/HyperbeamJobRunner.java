package com.github.nhirakawa.hyperbeam.hadoop;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.SequenceFileAsBinaryOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.nhirakawa.hyperbeam.color.Rgb;
import com.github.nhirakawa.hyperbeam.geometry.Coordinates;
import com.github.nhirakawa.hyperbeam.hadoop.writables.RgbWritable;
import com.github.nhirakawa.hyperbeam.scene.SceneGenerator;
import com.github.nhirakawa.hyperbeam.util.ObjectMapperInstance;
import com.google.common.base.Preconditions;

public class HyperbeamJobRunner {

  private static final Logger LOG = LoggerFactory.getLogger(HyperbeamJobRunner.class);

  private static final String SINGLE_FILENAME = "part-00000";

  public void run() throws Exception {
    HadoopJob hadoopJob = HadoopJob.builder()
        .setScene(SceneGenerator.generateCornellBox())
        .setStartTimestamp(Instant.now())
        .build();

    runJobAndWaitForSuccess(hadoopJob);

    List<Rgb> rgbs = new ArrayList<>();
    try (BufferedReader bufferedReader = openOutputFile(hadoopJob)) {
      while (bufferedReader.ready()) {
        rgbs.add(ObjectMapperInstance.instance().readValue(bufferedReader.readLine(), Rgb.class));
      }
    }

    BufferedImage bufferedImage = hadoopJob.getScene().getOutput().getEmptyBufferedImage();
    for (Rgb rgb : rgbs) {
      bufferedImage.setRGB(rgb.getCoordinates().getX(), rgb.getCoordinates().getY(), rgb.getColor().getRGB());
    }

    ImageIO.write(bufferedImage, "png", new File("hadoop-test.png"));
  }

  private void runJobAndWaitForSuccess(HadoopJob hadoopJob) throws IOException, InterruptedException, ClassNotFoundException {
    createAndWriteInputFiles(hadoopJob);

    JobConf jobConf = buildJobConf(hadoopJob);
    Job job = Job.getInstance(jobConf, hadoopJob.getJobName());

    boolean isOk = job.waitForCompletion(false);
    Preconditions.checkState(isOk, "Job did not complete successfully");
  }

  private JobConf buildJobConf(HadoopJob hadoopJob) {
    Configuration conf = new Configuration();
    JobConf jobConf = new JobConf(conf, HyperbeamJob.class);

    jobConf.setJarByClass(HyperbeamJob.class);

    jobConf.setMapperClass(HyperbeamMapper.class);

    jobConf.setMapOutputKeyClass(RgbWritable.class);
    jobConf.setMapOutputValueClass(NullWritable.class);

    jobConf.setReducerClass(HyperbeamReducer.class);
    jobConf.setNumReduceTasks(1);
    jobConf.setOutputKeyClass(RgbWritable.class);
    jobConf.setOutputValueClass(NullWritable.class);

    jobConf.setInputFormat(KeyValueTextInputFormat.class);

    FileInputFormat.addInputPath(jobConf, hadoopJob.getInputPath());
    SequenceFileAsBinaryOutputFormat.setSequenceFileOutputKeyClass(jobConf, RgbWritable.class);
    SequenceFileAsBinaryOutputFormat.setSequenceFileOutputValueClass(jobConf, NullWritable.class);
    FileOutputFormat.setOutputPath(jobConf, hadoopJob.getOutputPath());

    return jobConf;
  }

  private void createAndWriteInputFiles(HadoopJob hadoopJob) throws IOException {
    Files.createDirectories(Paths.get(hadoopJob.getInputPath().toString()));

    String uniqueId = UUID.randomUUID().toString() + "-" + hadoopJob.getStartTimestamp().toEpochMilli();

    java.nio.file.Path tempPath = Files.createTempFile(Paths.get(""), uniqueId, ".json");
    Files.write(tempPath, ObjectMapperInstance.instance().writeValueAsBytes(hadoopJob.getScene()), StandardOpenOption.WRITE);

    String inputPathName = String.format("%s/%s", hadoopJob.getInputPath(), uniqueId);
    java.nio.file.Path inputPath = Paths.get(inputPathName);
    Files.createFile(inputPath);

    for (int i = 0; i < hadoopJob.getScene().getOutput().getNumberOfRows(); i++) {
      for (int j = 0; j < hadoopJob.getScene().getOutput().getNumberOfColumns(); j++) {
        Coordinates coordinates = Coordinates.builder()
            .setX(i)
            .setY(j)
            .build();

        String line = String.format("%s\t%s", ObjectMapperInstance.instance().writeValueAsString(coordinates), tempPath.toAbsolutePath().toUri().toASCIIString());
        Files.write(inputPath, Collections.singleton(line), StandardOpenOption.APPEND);
      }
    }
  }

  private BufferedReader openOutputFile(HadoopJob hadoopJob) throws IOException {
    FileContext fileContext = FileContext.getFileContext();
    FSDataInputStream fsDataInputStream = fileContext.open(new Path(hadoopJob.getOutputPath() + "/" + SINGLE_FILENAME));

    return new BufferedReader(new InputStreamReader(fsDataInputStream, StandardCharsets.UTF_8));
  }

  public static void main(String... args) throws Exception {
    new HyperbeamJobRunner().run();
  }

}
