package com.github.nhirakawa.hyperbeam.config;

import com.github.nhirakawa.immutable.style.ImmutableStyle;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import org.immutables.value.Value;

@Value.Immutable
@ImmutableStyle
public interface ConfigWrapperModel {
  Config getConfig();

  @Value.Lazy
  default int getNumberOfSamples() {
    return getConfig().getInt(ConfigPaths.NUMBER_OF_SAMPLES.getPath());
  }

  @Value.Lazy
  default int getNumberOfThreads() {
    return getConfig().getInt(ConfigPaths.NUMBER_OF_THREADS.getPath());
  }

  @Value.Lazy
  default int getNumberOfRows() {
    return getConfig().getInt(ConfigPaths.NUMBER_OF_ROWS.getPath());
  }

  @Value.Lazy
  default int getNumberOfColumns() {
    return getConfig().getInt(ConfigPaths.NUMBER_OF_COLUMNS.getPath());
  }

  @Value.Lazy
  default String getOutFile() {
    return getConfig().getString(ConfigPaths.OUT_FILE.getPath());
  }

  @Value.Check
  default void validate() {
    for (ConfigPaths configPaths : ConfigPaths.values()) {
      Preconditions.checkState(
        getConfig().hasPath(configPaths.getPath()),
        "%s was not found",
        configPaths.getPath()
      );
    }
  }
}
