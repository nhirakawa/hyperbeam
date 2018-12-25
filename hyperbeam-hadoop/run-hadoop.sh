#!/bin/bash
set -e

parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
cd "$parent_path"

/usr/local/hadoop/bin/hadoop jar ./target/hyperbeam-hadoop-1.0-SNAPSHOT.jar
