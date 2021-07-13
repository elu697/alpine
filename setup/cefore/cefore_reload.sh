#!/bin/bash

# setup env
sudo apt -y update
sudo apt -y upgrade
sudo apt -y full-upgrade
sudo apt -y autoremove
sudo apt install -y unzip
sudo apt -y install make build-essential libssl-dev zlib1g-dev libbz2-dev libreadline-dev libsqlite3-dev wget curl llvm libncurses5-dev libncursesw5-dev xz-utils tk-dev libffi-dev liblzma-dev python-openssl git zlib1g-dev libffi-dev automake net-tools

# install cefore
cd cefore-0.8.3
export CEFORE_DIR=/usr/local
aclocal
autoconf
automake
./configure --enable-csmgr --enable-cefping --enable-samptp --enable-debug --enable-cache
make
sudo make install
sudo ldconfig

sleep 2
# run cefore
sudo cefnetdstop
sleep 1
sudo csmgrdstop
sleep 1
sudo csmgrdstarta
sleep 1
sudo cefnetdstart

csmgrstatus
cefstatus
