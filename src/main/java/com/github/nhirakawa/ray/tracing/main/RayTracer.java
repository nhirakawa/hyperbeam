package com.github.nhirakawa.ray.tracing.main;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.nhirakawa.ray.tracing.color.Rgb;
import com.github.nhirakawa.ray.tracing.geometry.Ray;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;
import com.github.nhirakawa.ray.tracing.image.PpmWriter;

public class RayTracer {

  private static final String FILENAME = "test.ppm";

  public static void main(String... args) throws Exception {
    int numberOfRows = 200;
    int numberOfColumns = 100;

//    List<Rgb> rgbs = buildRainbowRgbs(numberOfRows, numberOfColumns);
    List<Rgb> rgbs = buildBlueRgbs(numberOfRows, numberOfColumns);

    new PpmWriter().write(new File(FILENAME), numberOfRows, numberOfColumns, rgbs);
  }

  private static List<Rgb> buildRainbowRgbs(int numberOfRows, int numberOfColumns) {
    List<Rgb> rgbs = new ArrayList<>();
    for (int j = numberOfColumns - 1; j >= 0; j--) {
      for (int i = 0; i < numberOfRows; i++) {
        float r = (float) i / (float) numberOfRows;
        float g = (float) j / (float) numberOfColumns;
        float b = 0.2f;

        int ir = (int) (255.99 * r);
        int ig = (int) (255.99 * g);
        int ib = (int) (255.99 * b);

        rgbs.add(new Rgb(ir, ig, ib));
      }
    }
    return rgbs;
  }

  private static List<Rgb> buildBlueRgbs(int numberOfRows, int numberOfColumns) {
    List<Rgb> rgbs = new ArrayList<>();

    Vector3 lowerLeftCorner = new Vector3(-2, -1, -1);
    Vector3 horizontal = new Vector3(4, 0, 0);
    Vector3 vertical = new Vector3(0, 2, 0);
    Vector3 origin = new Vector3(0, 0, 0);

    for (int j = numberOfColumns - 1; j >= 0; j--) {
      for (int i = 0; i < numberOfRows; i++) {
        double u = (float) i / numberOfRows;
        double v = (float) j / numberOfColumns;

        Vector3 destination = lowerLeftCorner.add(horizontal.scalarMultiply(u)).add(vertical.scalarMultiply(v));

        Ray ray = new Ray(origin, destination);

        Vector3 color = color(ray);

        int ir = (int) (255.99 * color.getX());
        int ig = (int) (255.99 * color.getY());
        int ib = (int) (255.99 * color.getZ());

        rgbs.add(new Rgb(ir, ig, ib));
      }
    }

    return Collections.unmodifiableList(rgbs);
  }

  private static Vector3 color(Ray ray) {
    double sphereCollision = collideWithSphere(new Vector3(0, 0, -1), 0.5, ray);

    if (sphereCollision > 0) {
      Vector3 normal = ray.getPointAtParameter(sphereCollision).subtract(new Vector3(0, 0, -1)).unit();
      double x = normal.getX() + 1;
      double y = normal.getY() + 1;
      double z = normal.getZ() + 1;

      return new Vector3(x, y, z).scalarMultiply(0.5);
    }

    Vector3 unitDirection = ray.getDirection().unit();
    double t = 0.5 * (unitDirection.getY() + 1);

    Vector3 a = new Vector3(1, 1, 1).scalarMultiply(1 - t);
    Vector3 b = new Vector3(0.5, 0.7, 1.0).scalarMultiply(t);

    return a.add(b);
  }

  private static double collideWithSphere(Vector3 sphereCenter, double radius, Ray ray) {
    Vector3 oc = ray.getOrigin().subtract(sphereCenter);
    double a = ray.getDirection().dotProduct(ray.getDirection());
    double b = 2 * oc.dotProduct(ray.getDirection());
    double c = oc.dotProduct(oc) - (radius * radius);

    double discriminant = (b * b) - (4 * a * c);

    if (discriminant < 0) {
      return -1;
    } else {
      return (-b - Math.sqrt(discriminant)) / (2 * a);
    }

  }

}
