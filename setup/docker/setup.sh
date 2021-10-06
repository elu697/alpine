#!/bin/bash

set -x

# Add the package repositories
# sudo apt-get -y update
# sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common aptitude
# curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
# sudo apt-key fingerprint 0EBFCD88
# sudo add-apt-repository    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
#     $(lsb_release -cs) \
#     stable"
# sudo apt-get -y update
# sudo apt-get install -y docker-ce
# sudo docker version
# sudo usermod -aG docker $USER

# sudo add-apt-repository ppa:graphics-drivers/ppa
# sudo aptitude -y update
# sudo aptitude -y install ubuntu-drivers-common
# sudo aptitude -y install nvidia-driver-470-server

# distribution=$(. /etc/os-release;echo $ID$VERSION_ID)
# curl -s -L https://nvidia.github.io/nvidia-docker/gpgkey | sudo apt-key add -
# curl -s -L https://nvidia.github.io/nvidia-docker/$distribution/nvidia-docker.list | sudo tee /etc/apt/sources.list.d/nvidia-docker.list
# sudo apt-get update && sudo apt-get install -y nvidia-docker2
# sudo systemctl restart docker

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


