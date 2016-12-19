#!/usr/bin/env bash
mvn clean install -Dmaven.test.skip=true
scp target/java2kafkaTest-1.0.jar.original zhaogj@192.168.10.10:~/java2Kafka/lib/java2kafkaTest-1.0.jar
