package com.github.nhirakawa.ray.tracing.texture;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.immutables.value.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.github.nhirakawa.ray.tracing.geometry.Vector3;

@Value.Immutable
@ImmutableStyle
public abstract class ImageTextureModel implements Texture {

  public abstract URL getImageUrl();

  @Value.Lazy
  @JsonIgnore
  public BufferedImage getBufferedImage() {
    try {
      return ImageIO.read(getImageUrl());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Vector3 getValue(double u, double v, Vector3 point) {
    BufferedImage bufferedImage = getBufferedImage();

    int i = (int) (u * bufferedImage.getWidth());
    int j = (int) ((1 - v) * (bufferedImage.getHeight()));

    i = Math.max(i, 0);
    j = Math.max(j, 0);

    i = Math.min(i, bufferedImage.getWidth() - 1);
    j = Math.min(j, bufferedImage.getHeight() - 1);

    int rgb = bufferedImage.getRGB(i, j);

    Color color = new Color(rgb);

    return new Vector3(color.getRed(), color.getGreen(), color.getBlue()).scalarDivide(255);
  }

  @Override
  @Value.Auxiliary
  public TextureType getTextureType() {
    return TextureType.IMAGE;
  }

}
