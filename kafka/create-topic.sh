#!/bin/bash
$KAFKA_HOME/bin/kafka-topics.sh --create --topic flink-events --bootstrap-server localhost:9092
