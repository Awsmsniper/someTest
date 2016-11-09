#!/bin/bash
mvn clean install -Dmaven.test.skip=true
#scp -P 36044 target/idDatabaseWebService-1.1.jar qzt_java@121.14.204.68:~/idDatabase/bin/
#scp -P 36044 target/idDatabaseWebService-1.1.jar.original qzt_java@121.14.204.68:~/idDatabase/lib/idDatabaseWebService-1.1.jar