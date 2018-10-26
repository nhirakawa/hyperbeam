package com.github.nhirakawa.ray.tracing.image;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.github.nhirakawa.ray.tracing.color.Rgb;
import com.google.common.collect.Iterables;

public class PpmWriter {

  public PpmWriter() {

  }

  public void write(File file, int numberOfRows, int numberOfColumns, List<Rgb> rgbs) throws IOException {
    try (FileWriter fileWriter = new FileWriter(file)) {
      String header = String.format("P3%n%d %d%n255%n", numberOfRows, numberOfColumns);
      fileWriter.write(header);

      Iterable<List<Rgb>> partitionedRgbs = Iterables.partition(rgbs, numberOfColumns);

      for (List<Rgb> partition : partitionedRgbs) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Rgb rgb : partition) {
          stringBuilder.append(rgb.getRed());
          stringBuilder.append(" ");
          stringBuilder.append(rgb.getGreen());
          stringBuilder.append(" ");
          stringBuilder.append(rgb.getBlue());
          stringBuilder.append(" ");
        }
        stringBuilder.append("\n");
        fileWriter.write(stringBuilder.toString());
      }
    }
  }

}
