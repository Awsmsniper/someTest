#!/bin/bash
mvn clean install -Dmaven.test.skip=true
scp -P 36044 target/sparkByScalaTest-1.0-SNAPSHOT.jar qzt_java@121.14.204.68:~/spark/
