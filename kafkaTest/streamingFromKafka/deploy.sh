#!/bin/bash
mvn clean install -Dmaven.test.skip=true
scp target/streamingFromKafkaTest-1.0-SNAPSHOT.jar zhaogj@192.168.10.10:~/spark/