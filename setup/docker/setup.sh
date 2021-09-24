#!/bin/bash

set -x

sudo apt-get -y update
sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common aptitude
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo apt-key fingerprint 0EBFCD88
sudo add-apt-repository    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
    $(lsb_release -cs) \
    stable"
sudo apt-get -y update
sudo apt-get install -y docker-ce
sudo docker version
sudo usermod -aG docker $USER

sudo add-apt-repository ppa:graphics-drivers/ppa
sudo aptitude -y update
sudo aptitude -y install ubuntu-drivers-common
sudo aptitude -y install nvidia-driver-470-server


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


