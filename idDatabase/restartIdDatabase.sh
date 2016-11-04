#!/bin/bash
for i in `ps aux|grep idDatabase | grep 36162 | awk '{print$2}'`
do
  echo "kill -9 "$i
  kill -9 $i
done
java -jar -Dserver.port=36162 idDatabase-1.1.jar > /dev/null 2>&1 &
echo "idDatabase restart success"