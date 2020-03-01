package com.github.nhirakawa.hyperbeam;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

public final class Metrics {
  private static final MetricRegistry METRIC_REGISTRY = new MetricRegistry();

  private Metrics() {}

  public static Timer getTimer(String name) {
    return METRIC_REGISTRY.timer(name);
  }

  public static MetricRegistry instance() {
    return METRIC_REGISTRY;
  }
}
