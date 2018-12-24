package com.github.nhirakawa.hyperbeam.storm;

import java.util.Collections;

import org.apache.storm.LocalCluster;
import org.apache.storm.LocalDRPC;
import org.apache.storm.drpc.LinearDRPCTopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalModeTest {

  private static final Logger LOG = LoggerFactory.getLogger(LocalModeTest.class);

  public static void main(String... args) {
    LocalDRPC localDRPC = new LocalDRPC();
    LocalCluster localCluster = new LocalCluster();

    LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("exclamation");
    builder.addBolt(new ExclaimBolt(), 3);

    localCluster.submitTopology("drpc-demo", Collections.emptyMap(), builder.createLocalTopology(localDRPC));

    LOG.info("Results for 'hello': {}", localDRPC.execute("exclamation", "hello"));

    localCluster.shutdown();
    localDRPC.shutdown();
  }

}
