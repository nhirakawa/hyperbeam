#!/bin/bash
set -e

parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path"

HADOOP_OPTS="-XX:-UseGCOverheadLimit" HADOOP_CLIENT_OPTS="-XX:-UseGCOverheadLimit -Xmx1g" JAVA_HOME=/usr /usr/local/hadoop/bin/hadoop jar ./target/hyperbeam-hadoop-1.0-SNAPSHOT.jar -Xmx4g
