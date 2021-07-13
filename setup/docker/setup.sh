#!/bin/bash

docker-compose build
docker-compose up -d

# host bufer tuning

if [ $(uname) = Darwin ]; then
sudo sysctl net.local.stream.sendspace=2000000
sudo sysctl net.local.stream.recvspace=2000000
else
sudo sysctl --write net.core.rmem_default=10000000
sudo sysctl --write net.core.wmem_default=10000000
sudo sysctl --write net.core.rmem_max=10000000
sudo sysctl --write net.core.wmem_max=10000000
fi


