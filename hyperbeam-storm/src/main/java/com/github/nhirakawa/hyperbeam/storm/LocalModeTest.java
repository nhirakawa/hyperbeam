package com.github.nhirakawa.hyperbeam.storm;

import org.apache.storm.LocalCluster;

public class LocalModeTest {

  public static void main(String... args) {
    LocalCluster localCluster = new LocalCluster();

    localCluster.shutdown();
  }

}
