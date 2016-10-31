#!/bin/bash
mvn clean install -Dmaven.test.skip=true
scp -P 36044 target/esTest-0.0.1-SNAPSHOT.jar qzt_java@gdba.qzt360.com:~/tmp/
