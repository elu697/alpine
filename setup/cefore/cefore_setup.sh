#!/bin/bash
# install cefore
wget https://cefore.net/dlfile.php?file=cefore-0.8.3.zip -O cefore.zip
unzip cefore.zip
cd cefore-0.8.3
export CEFORE_DIR=/usr/local
aclocal
automake
./configure --enable-csmgr --enable-cache
make
sudo make install
ldconfig

# copy settings
cp -f cefnetd.conf /usr/local/cefore/cefnetd.conf
cp -f csmgrd.conf /usr/local/cefore/csmgrd.conf

# tuning for cefore
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
