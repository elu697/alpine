#!/bin/bash

if [ $# -ne 2 ]; then
  echo "指定された引数は$#個です。" 1>&2
  echo "実行するには2個の引数が必要です。port name" 1>&2
  exit 1
fi

echo $0 $1 $2

# build docker image.
docker build -t $2 .

# # run docker container
docker run -itd -p $1:22 --name $2 $2

# run docker container on host
#docker run -itd --network host --name $2 $2
