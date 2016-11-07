#!/bin/bash
mvn clean install -Dmaven.test.skip=true
#scp -P 36044 target/baSystemWebService-1.2.jar qzt_java@121.14.204.68:~/baSystemWebService/bin/
scp -P 36044 target/baSystemWebService-1.2.jar.original qzt_java@121.14.204.68:~/baSystemWebService/lib/baSystemWebService-1.2.jar