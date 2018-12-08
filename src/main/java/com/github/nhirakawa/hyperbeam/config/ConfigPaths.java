package com.github.nhirakawa.hyperbeam.config;

public enum ConfigPaths {

  NUMBER_OF_SAMPLES("hyperbeam.samples"),
  NUMBER_OF_THREADS("hyperbeam.threads"),
  NUMBER_OF_ROWS("hyperbeam.dimensions.rows"),
  NUMBER_OF_COLUMNS("hyperbeam.dimensions.columns"),
  OUT_FILE("hyperbeam.out"),
  ;

  private final String path;

  ConfigPaths(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

}
