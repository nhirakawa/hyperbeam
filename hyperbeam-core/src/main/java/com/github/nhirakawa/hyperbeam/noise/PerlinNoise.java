package com.github.nhirakawa.hyperbeam.noise;

import static com.github.nhirakawa.hyperbeam.util.MathUtils.rand;

import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public final class PerlinNoise {
  private static final List<Vector3> RANDOM_VECTORS = generateRandomUnitVectors();

  private static final List<Integer> PERMUTED_X = generatePermutatedIntegers();
  private static final List<Integer> PERMUTED_Y = generatePermutatedIntegers();
  private static final List<Integer> PERMUTED_Z = generatePermutatedIntegers();

  private PerlinNoise() {}

  public static double noise(Vector3 point) {
    double u = point.getX() - Math.floor(point.getX());
    double v = point.getY() - Math.floor(point.getY());
    double w = point.getZ() - Math.floor(point.getZ());

    int i = (int) StrictMath.floor(point.getX());
    int j = (int) StrictMath.floor(point.getY());
    int k = (int) StrictMath.floor(point.getZ());

    Vector3[][][] what = new Vector3[2][2][2];

    Set<Integer> integers = ImmutableSet.of(0, 1);
    Set<List<Integer>> products = Sets.cartesianProduct(
      integers,
      integers,
      integers
    );
    for (List<Integer> product : products) {
      Preconditions.checkState(product.size() == 3);

      int di = product.get(0);
      int dj = product.get(1);
      int dk = product.get(2);

      int permutedI = PERMUTED_X.get((i + di) & 255);
      int permutedJ = PERMUTED_Y.get((j + dj) & 255);
      int permutedK = PERMUTED_Z.get((k + dk) & 255);

      Vector3 value = RANDOM_VECTORS.get(permutedI ^ permutedJ ^ permutedK);
      what[di][dj][dk] = value;
    }

    return trilinearInterpolate(what, u, v, w);
  }

  public static double turbulence(Vector3 point) {
    return turbulence(point, 7);
  }

  public static double turbulence(Vector3 point, int depth) {
    double accumulate = 0;
    Vector3 temp = point;
    double weight = 1;

    for (int i = 0; i < depth; i++) {
      accumulate += weight * noise(temp);
      weight *= 0.5;
      temp = temp.scalarMultiply(2);
    }

    return Math.abs(accumulate);
  }

  private static double trilinearInterpolate(
    Vector3[][][] what,
    double u,
    double v,
    double w
  ) {
    double accumulate = 0;

    double uu = u * u * (3 - 2 * u);
    double vv = v * v * (3 - 2 * v);
    double ww = w * w * (3 - 2 * w);

    Set<Integer> integers = ImmutableSet.of(0, 1);

    Set<List<Integer>> products = Sets.cartesianProduct(
      integers,
      integers,
      integers
    );
    for (List<Integer> product : products) {
      Preconditions.checkState(product.size() == 3);

      int i = product.get(0);
      int j = product.get(1);
      int k = product.get(2);

      double first = (i * uu) + ((1 - i) * (1 - uu));
      double second = (j * vv) + ((1 - j) * (1 - vv));
      double third = (k * ww) + ((1 - k) * (1 - ww));

      Vector3 weight = Vector3
        .builder()
        .setX(u - i)
        .setY(v - j)
        .setZ(w - k)
        .build();

      accumulate += first * second * third * what[i][j][k].dotProduct(weight);
    }

    return accumulate;
  }

  private static List<Integer> generatePermutatedIntegers() {
    List<Integer> sortedIntegers = IntStream
      .range(0, 256)
      .boxed()
      .collect(ImmutableList.toImmutableList());

    return permute(sortedIntegers);
  }

  private static List<Integer> permute(List<Integer> integers) {
    List<Integer> result = new ArrayList<>(integers);

    for (int i = integers.size() - 1; i > 0; i--) {
      int target = (int) (rand() * (i + 1));
      int temp = integers.get(i);

      result.set(i, result.get(target));
      result.set(target, temp);
    }

    return result;
  }

  private static List<Vector3> generateRandomUnitVectors() {
    List<Vector3> vectors = new ArrayList<>();
    for (int i = 0; i < 256; i++) {
      Vector3 rand = Vector3
        .builder()
        .setX(-1 + 2 * rand())
        .setY(-1 + 2 * rand())
        .setZ(-1 + 2 * rand())
        .build();

      vectors.add(rand);
    }
    return Collections.unmodifiableList(vectors);
  }
}
