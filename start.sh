#!/usr/bin/env bash


git checkout local_storage_jiemo
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


#
#安装 Node.js
#安装 marked：npm install marked --save
#在 Solo 目录下执行 node js/marked/http.js 以启动 markdown 解析引擎（你可能需要 nohup），需要 8250 端口可用
#重启 Solo 后就会使用 marked 作为 markdown 渲染引擎了
