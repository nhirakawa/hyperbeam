package com.github.nhirakawa.hyperbeam.geometry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.data.Offset;
import org.junit.Test;

public class Vector3Test {

  private static final Offset<Double> OFFSET = Offset.offset(0.0001);

  @Test
  public void testDotProduct() {
    Vector3 first = Vector3.builder()
        .setX(1)
        .setY(3)
        .setZ(-5)
        .build();

    Vector3 second = Vector3.builder()
        .setX(4)
        .setY(-2)
        .setZ(-1)
        .build();

    double dotProduct = first.dotProduct(second);

    assertThat(dotProduct).isCloseTo(3, OFFSET);
  }

  @Test
  public void testCrossProduct() {
    Vector3 a = Vector3.builder()
        .setX(2)
        .setY(3)
        .setZ(4)
        .build();

    Vector3 b = Vector3.builder()
        .setX(5)
        .setY(6)
        .setZ(7)
        .build();

    Vector3 c = a.cross(b);

    assertThat(c.getX()).isCloseTo(-3, OFFSET);
    assertThat(c.getY()).isCloseTo(6, OFFSET);
    assertThat(c.getZ()).isCloseTo(-3, OFFSET);
  }

  @Test
  public void testGetI() {
    Vector3 vector = Vector3.builder()
        .setX(10)
        .setY(20)
        .setZ(30)
        .build();

    assertThat(vector.get(0)).isCloseTo(vector.getX(), OFFSET);
    assertThat(vector.get(1)).isCloseTo(vector.getY(), OFFSET);
    assertThat(vector.get(2)).isCloseTo(vector.getZ(), OFFSET);

    assertThatThrownBy(() -> vector.get(1000))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(() -> vector.get(-1))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void testAdd() {
    Vector3 a = Vector3.builder()
        .setX(100)
        .setY(200)
        .setZ(300)
        .build();

    Vector3 b = Vector3.builder()
        .setX(50)
        .setY(40)
        .setZ(30)
        .build();

    Vector3 add = a.add(b);

    assertThat(add.getX()).isCloseTo(a.getX() + b.getX(), OFFSET);
    assertThat(add.getY()).isCloseTo(a.getY() + b.getY(), OFFSET);
    assertThat(add.getZ()).isCloseTo(a.getZ() + b.getZ(), OFFSET);
  }

}
