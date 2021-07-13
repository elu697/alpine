#!/bin/bash

# install cefore
# wget https://cefore.net/dlfile.php?file=cefore-0.8.3.zip -O cefore-0.8.3.zip
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

git config --global user.email "elyou.dev@gmail.com"
git config --global user.name "elu697"

# rm -rf cefore-0.8.3
# rm cefore.zip
