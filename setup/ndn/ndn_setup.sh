#!/bin/bash
set -Ceuxo pipefail

# rm -rf NFD/
# rm -rf ndn-cxx/
# rm -rf ndn-tools/
# rm -rf jndn/

git config --global user.email "elyou.dev@gmail.com"
git config --global user.name "elu697"

# Download ndn-cxx
git clone --recursive https://github.com/named-data/ndn-cxx.git
# Download NFD
git clone --recursive https://github.com/named-data/NFD.git
# Download ndn-tools
git clone --recursive https://github.com/named-data/ndn-tools.git
# Download jndn
git clone --recursive https://github.com/named-data/jndn.git

sudo aptitude update -y
# sudo aptitude upgrade -y

sudo aptitude install -y openjdk-11-jdk
sudo update-alternatives --config java

# # ndn-cxx
# # https://github.com/named-data/ndn-cxx/blob/master/docs/INSTALL.rst
sudo aptitude install -y g++ pkg-config python3-minimal libboost-all-dev libssl-dev libsqlite3-dev
sudo aptitude install -y doxygen graphviz python3-pip
sudo pip3 install sphinx sphinxcontrib-doxylink
cd ndn-cxx
./waf configure --with-examples  # on CentOS, add --without-pch
./waf
sudo ./waf install
sudo ldconfig  # on Linux only
cd ..

# # NFD
sudo aptitude install -y software-properties-common
sudo add-apt-repository ppa:named-data/ppa
sudo apt update -y
sudo aptitude install -y libpcap-dev libsystemd-dev
# sudo aptitude install -y nfd
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
./waf configure
./waf
sudo ./waf install
cd ..

sudo nfd-start

# jdn
sudo aptitude install -y software-properties-common
sudo aptitude install -y maven
cd jndn
mvn package
mvn test -P with-integration-tests
mvn install
cd ..

# python-ndn
# pip3 install python-ndn nest-asyncio
pip3 install -r requirements.txt

cd ../../nln
./package.sh

# EX CONFIG
# nfdc strategy set / /localhost/nfd/strategy/multicast/v=4
# nfdc strategy set / /localhost/nfd/strategy/multicast/v=4
# prefix= / strategy= /localhost/nfd/strategy/best-route/v=5
# prefix= /localhost strategy= /localhost/nfd/strategy/multicast/v=4
# prefix= /ndn/broadcast strategy= /localhost/nfd/strategy/multicast/v=4
# prefix= /localhost/nfd strategy= /localhost/nfd/strategy/best-route/v=5
# prefix= /localhop/ndn-autoconf/hub strategy= /localhost/nfd/strategy/multicast/v=4
