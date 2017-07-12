#!/usr/bin/env bash


git checkout local_storage
git pull

result=`ps -ef|grep -v grep |grep org.b3log.solo.Starter`

echo ${result}
if [[ ${result} != "" ]];then
    echo "存在该线程  不能开启"

    kill -9 `echo ${result}|awk '{print $2}'`


fi


mvn clean package  -Dmaven.test.skip=true

rm -rf webapp

mkdir webapp

cp target/solo.war webapp/

cd webapp

mkdir images/upload


ln -s /data/uploadContent  images/upload

unzip solo.war


nohup java -cp WEB-INF/lib/*:WEB-INF/classes org.b3log.solo.Starter  &