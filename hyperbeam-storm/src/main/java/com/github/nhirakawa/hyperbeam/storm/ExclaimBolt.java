package com.github.nhirakawa.hyperbeam.storm;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExclaimBolt extends BaseBasicBolt {

  private static final Logger LOG = LoggerFactory.getLogger(ExclaimBolt.class);

  @Override
  public void execute(Tuple tuple, BasicOutputCollector collector) {
    String input = tuple.getString(1);
    collector.emit(new Values(tuple.getValue(0), input + "!"));
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declare(new Fields("id", "result"));
  }

}
