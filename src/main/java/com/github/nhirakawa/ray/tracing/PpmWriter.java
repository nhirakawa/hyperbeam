package com.github.nhirakawa.ray.tracing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

  public static void main(String... args) throws IOException {
    int nx = 200;
    int ny = 100;

    List<Rgb> rgbs = new ArrayList<>();
    for (int j = ny - 1; j >= 0; j--) {
      for (int i = 0; i < nx; i++) {
        float r = (float) i / (float) nx;
        float g = (float) j / (float) ny;
        float b = 0.2f;

        int ir = (int) (255.99 * r);
        int ig = (int) (255.99 * g);
        int ib = (int) (255.99 * b);

        rgbs.add(new Rgb(ir, ig, ib));
      }
    }

    new PpmWriter().write(new File("test.ppm"), nx, ny, rgbs);
  }

}
