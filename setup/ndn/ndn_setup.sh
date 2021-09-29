#!/bin/bash

# Download ndn-cxx
git clone https://github.com/named-data/ndn-cxx.git
# Download NFD
git clone --recursive https://github.com/named-data/NFD.git
# Download ndn-tools
git clone https://github.com/named-data/ndn-tools.git
# Download jndn
git clone https://github.com/named-data/jndn.git

# sudo aptitude update -y
# sudo aptitude upgrade -y

# # ndn-cxx
# # https://github.com/named-data/ndn-cxx/blob/master/docs/INSTALL.rst
sudo aptitude install -y g++ pkg-config python3-minimal libboost-all-dev libssl-dev libsqlite3-dev
sudo aptitude install -y doxygen graphviz python3-pip
sudo pip3 install sphinx sphinxcontrib-doxylink tensorflow
cd ndn-cxx
./waf configure --with-examples  # on CentOS, add --without-pch
./waf
sudo ./waf install
sudo ldconfig  # on Linux only
cd ..

# # NFD
sudo aptitude install -y software-properties-common
sudo add-apt-repository ppa:named-data/ppa
sudo aptitude install -y libpcap-dev libsystemd-dev
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

# jdn
sudo aptitude install -y maven
cd jndn
mvn package
mvn -f pom-without-protobuf.xml package
mvn test -P with-integration-tests
mvn install
cd ..

# python-ndn
pip3 install python-ndn
