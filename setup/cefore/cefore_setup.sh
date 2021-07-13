#!/bin/bash

# setup env
sudo apt -y update
sudo apt -y upgrade
sudo apt -y full-upgrade
sudo apt -y autoremove
sudo apt install -y unzip
sudo apt -y install make build-essential libssl-dev zlib1g-dev libbz2-dev libreadline-dev libsqlite3-dev wget curl llvm libncurses5-dev libncursesw5-dev xz-utils tk-dev libffi-dev liblzma-dev python-openssl git zlib1g-dev libffi-dev automake net-tools

# install cefore
wget https://cefore.net/dlfile.php?file=cefore-0.8.3.zip -O cefore-0.8.3.zip
unzip -o cefore-0.8.3.zip
cd cefore-0.8.3
export CEFORE_DIR=/usr/local
aclocal
automake
./configure --enable-csmgr --enable-cefping --enable-samptp --enable-debug --enable-cache
make
sudo make install
sudo ldconfig

# copy settings
cd ..
cp -f config/cefnetd.conf /usr/local/cefore/cefnetd.conf
cp -f config/csmgrd.conf /usr/local/cefore/csmgrd.conf

# tuning for cefore without on docker
sudo sysctl --write net.core.rmem_default=10000000
sudo sysctl --write net.core.wmem_default=10000000
sudo sysctl --write net.core.rmem_max=10000000
sudo sysctl --write net.core.wmem_max=10000000

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


rm -rf cefore-0.8.3
rm cefore.zip
