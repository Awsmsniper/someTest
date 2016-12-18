#!/usr/bin/env bash
mvn clean install -Dmaven.test.skip=true
scp target/java2kafkaTest-1.0.jar zhaogj@192.168.3.152:~/
