#!/bin/bash

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
sudo csmgrdstart
sleep 1
sudo cefnetdstart

csmgrstatus
cefstatus
