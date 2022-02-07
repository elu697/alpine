#!/bin/bash
set -Cux
export DEBIAN_FRONTEND=noninteractive
rm -rf NFD/
rm -rf ndn-cxx/
rm -rf ndn-tools/
rm -rf jndn/
PASSWORD=password
echo $PASSWORD | sudo -S ls

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

sudo dpkg --configure -a
sudo aptitude update -y
sudo -E aptitude upgrade -y
sudo aptitude install -y make libjsoncpp-dev libjsonrpccpp-dev libgmp-dev libreadline-dev libmicrohttpd-dev ocl-icd-libopencl1 \
  opencl-headers mesa-common-dev build-essential libboost-all-dev libsm6 libxext6 libxrender-dev libssl-dev \
  zlib1g-dev libbz2-dev libreadline-dev libsqlite3-dev wget curl llvm libncurses5-dev libncursesw5-dev xz-utils tk-dev \
  libffi-dev liblzma-dev python-openssl zlib1g-dev libffi-dev automake libsystemd-dev libusb-dev cmake gstreamer1.0 libglib2.0-dev libreadline-dev libudev-dev

sudo aptitude install -y openjdk-11-jdk
sudo update-alternatives --config java

# # ndn-cxx
# # https://github.com/named-data/ndn-cxx/blob/master/docs/INSTALL.rst
sudo aptitude install -y g++ pkg-config python3-minimal libboost-all-dev libssl-dev libsqlite3-dev
sudo aptitude install -y doxygen graphviz python3-pip
sudo pip3 install sphinx sphinxcontrib-doxylink
cd ndn-cxx
./waf configure
./waf
sudo ./waf install
sudo ldconfig  # on Linux only
cd ..

# # NFD
sudo aptitude update -y
sudo aptitude install -y software-properties-common
sudo add-apt-repository -y ppa:named-data/ppa
sudo aptitude update -y
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
sudo aptitude update -y
sudo aptitude install -y default-jdk
sudo aptitude install -y psmisc
sudo aptitude install -y libpcap-dev
cd ndn-tools
./waf configure
./waf
sudo ./waf install
cd ..

sudo nfd-start

# jdn
sudo aptitude update -y
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
# ./package.sh

# EX CONFIG
# nfdc strategy set / /localhost/nfd/strategy/multicast/v=4
# nfdc strategy set / /localhost/nfd/strategy/multicast/v=4
# prefix= / strategy= /localhost/nfd/strategy/best-route/v=5
# prefix= /localhost strategy= /localhost/nfd/strategy/multicast/v=4
# prefix= /ndn/broadcast strategy= /localhost/nfd/strategy/multicast/v=4
# prefix= /localhost/nfd strategy= /localhost/nfd/strategy/best-route/v=5
# prefix= /localhop/ndn-autoconf/hub strategy= /localhost/nfd/strategy/multicast/v=4
