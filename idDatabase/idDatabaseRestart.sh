#!/bin/bash
# Created by zhaogj on 05/11/2016.

# 脚本名称
PRG="$0"
# echo "PRG=$PRG"

# 脚本所在目录
BIN=`cd $(dirname "$PRG"); pwd`
echo "BIN=$BIN"

HOME=`dirname "$BIN"`
echo "HOME=$HOME"

# 端口
PORT="$1"

# 如果没指定，那就给默认值
if [ ! $PORT ];then
  PORT=36160
fi

# 如果指定了端口，检查一下是否符合格式
if [ "$PORT" -gt 0 -a "$PORT" -lt 65535 ] 2>/dev/null ;then
  echo "PORT=$PORT"
else
  echo "PORT must a num, (0-65535)"
  exit
fi

# 找到所有lib下的依赖包
LIB=`find ${HOME}/lib/ -name "*.jar"`
echo "LIB=$LIB"

# 日志目录
LOG=${HOME}/logs/
echo "LOG=$LOG"

PIDFILE=${HOME}/pidfile
echo "PIDFILE=$PIDFILE"

classpath="."
classpath=$classpath:$CONF
for item in $LIB
do
  classpath=$classpath:$item
done
echo "classpath=$classpath"

JVM_OPTS="-server -Xms4G -Xmx4G -XX:MaxPermSize=512M -XX:PermSize=512M -Xloggc:$LOG/gc.log -XX:-PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70  -XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled -XX:+HeapDumpOnOutOfMemoryError"

#for i in `ps aux|grep idDatabase | grep $PORT | awk '{print$2}'`
#do
#  echo "kill -9 "$i
#  kill -9 $i
#done


#if [ -f $PIDFILE ];then
#  PID=`cat $PIDFILE`
#  tr=`jps -v | grep $PID | grep $PORT | grep $HOME`
#  echo "tr=$tr"
#  if [ "$tr" != "" ];then
#    echo "kill $PID"
#    kill $PID
#  fi
#fi
sleep 3
