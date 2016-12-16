#!/bin/bash
mvn clean install -Dmaven.test.skip=true
#scp -P 36044 target/baSystemDataService-1.0.jar.original qzt_java@121.14.204.68:~/baSystemDataService/lib/baSystemDataService-1.0.jar