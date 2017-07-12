#!/usr/bin/env bash

result=`ps -ef|grep -v grep |grep org.b3log.solo.Starter`

echo ${result}
if [[ ${result} != "" ]];then
    echo "存在该线程  不能开启"

    kill -9 `echo ${result}|awk '{print $2}'`

    exit 0;

fi


mvn clean package  -Dmaven.test.skip=true

rm -rf webapp

mkdir webapp

cp target/solo.war webapp/

cd webapp

mkdir /data/uploadContent

ln -s /data/uploadContent  uploadContent

unzip solo.war


nohup java -cp WEB-INF/lib/*:WEB-INF/classes org.b3log.solo.Starter  &