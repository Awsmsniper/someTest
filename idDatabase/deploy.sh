#!/bin/bash
rm -f ./idDatabaseLog.txt
mvn clean install -Dmaven.test.skip=true
scp -P 36044 target/idDatabase-1.1.jar qzt_java@121.14.204.68:~/jar/
