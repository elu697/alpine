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
./configure --enable-csmgr --enable-cache --enable-cefping --enable-cefinfo --enable-debug --enable-samptp
make
sudo make install
sudo ldconfig

# run cefore
sudo cefnetdstop
sleep 1
sudo csmgrdstop
sleep 1
sudo csmgrdstart
sleep 1
sudo cefnetdstart

csmgrstatus
cefstatus
