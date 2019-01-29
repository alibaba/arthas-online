#!/bin/sh

nohup java -jar /opt/arthas/arthas-demo.jar &

JAVA_PID=$(jps | grep arthas-demo | awk '{print $1}')
echo $JAVA_PID

java -jar /opt/arthas/arthas-boot.jar --attach-only $JAVA_PID --target-ip 0.0.0.0 --session-timeout 60


for i in `seq 1 500000` ; 
do
  echo "sleep 30s, then check connection count."
  sleep 3000;
  connect_count=`netstat -antp | grep ESTABLISHED | grep ':8563' | wc -l`
  echo "connection count: $connect_count"
  if [[ $connect_count == 0 ]]; then
    halt
  fi
done


# tail -f /dev/null
