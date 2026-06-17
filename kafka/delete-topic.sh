#!/bin/bash
$KAFKA_HOME/bin/kafka-topics.sh --delete --topic flink-events --bootstrap-server localhost:9092
