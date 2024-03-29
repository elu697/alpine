#!/bin/bash
# install cefore
cd ../../cefore
export CEFORE_DIR=/usr/local
aclocal
automake
./configure --enable-csmgr --enable-cefping --enable-samptp --enable-debug --enable-cache
make
sudo make install
sudo ldconfig

# copy settings
cd ..
ls
cp -f setup/cefore/config/cefnetd.conf /usr/local/cefore/cefnetd.conf
cp -f setup/cefore/config/csmgrd.conf /usr/local/cefore/csmgrd.conf
cp -f setup/cefore/config/plugin.conf /usr/local/cefore/plugin.conf

# tuning for cefore without on docker
sudo sysctl --write net.core.rmem_default=10000000
sudo sysctl --write net.core.wmem_default=10000000
sudo sysctl --write net.core.rmem_max=10000000
sudo sysctl --write net.core.wmem_max=10000000

# run cefore
sleep 2
sudo cefnetdstop
sleep 1
sudo csmgrdstop
sleep 1
sudo csmgrdstart
sleep 1
sudo cefnetdstart

csmgrstatus
cefstatus

# install cefpyco
cd cefpyco
sudo apt install -y aptitude
sudo aptitude install -y cmake python-pip
pip install setuptools click numpy
cmake .
sudo make install

# original
git config --global user.email "elyou.dev@gmail.com"
git config --global user.name "elu697"

