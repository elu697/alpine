#!/bin/bash
set -Ceuxo pipefail

# Download ndn-cxx
git clone --recursive https://github.com/named-data/ndn-cxx.git
# Download NFD
git clone --recursive https://github.com/named-data/NFD.git
# Download ndn-tools
git clone --recursive https://github.com/named-data/ndn-tools.git
# Download jndn
git clone --recursive https://github.com/named-data/jndn.git

sudo aptitude update -y
sudo aptitude upgrade -y

# # ndn-cxx
# # https://github.com/named-data/ndn-cxx/blob/master/docs/INSTALL.rst
sudo aptitude install -y g++ pkg-config python3-minimal libboost-all-dev libssl-dev libsqlite3-dev
sudo aptitude install -y doxygen graphviz python3-pip
sudo pip3 install sphinx sphinxcontrib-doxylink
cd ndn-cxx
git reset --hard
git pull
./waf configure --with-examples  # on CentOS, add --without-pch
./waf
sudo ./waf install
sudo ldconfig  # on Linux only
cd ..

# # NFD
git reset --hard
git pull
sudo aptitude install -y software-properties-common
sudo add-apt-repository ppa:named-data/ppa
sudo aptitude install -y libpcap-dev libsystemd-dev
sudo aptitude nfd
cd NFD
./waf configure
./waf
sudo ./waf install
sudo cp /usr/local/etc/ndn/nfd.conf.sample /usr/local/etc/ndn/nfd.conf
cd ..

# ndn tool
# https://github.com/named-data/ndn-tools/blob/master/INSTALL.md
sudo aptitude install -y default-jdk
sudo aptitude install -y maven
sudo aptitude install -y psmisc

sudo aptitude install -y libpcap-dev
cd ndn-tools
git reset --hard
git pull
./waf configure
./waf
sudo ./waf install
cd ..

# jdn
sudo apt install software-properties-common
sudo aptitude install -y maven
cd jndn
git reset --hard
git pull
mvn package
mvn -f pom-without-protobuf.xml package
mvn test -P with-integration-tests
mvn install
cd ..

# python-ndn
# pip3 install python-ndn nest-asyncio
pip3 install -r requirements.txt


# EX CONFIG
nfd-start
nfdc strategy set / /localhost/nfd/strategy/multicast/v=4
# nfdc strategy set / /localhost/nfd/strategy/multicast/v=4
# prefix= / strategy= /localhost/nfd/strategy/best-route/v=5
# prefix= /localhost strategy= /localhost/nfd/strategy/multicast/v=4
# prefix= /ndn/broadcast strategy= /localhost/nfd/strategy/multicast/v=4
# prefix= /localhost/nfd strategy= /localhost/nfd/strategy/best-route/v=5
# prefix= /localhop/ndn-autoconf/hub strategy= /localhost/nfd/strategy/multicast/v=4
