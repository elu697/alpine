#!/bin/bash

# ndn core
sudo aptitude install -y software-properties-common
sudo add-apt-repository ppa:named-data/ppa
sudo apt update -y
sudo apt install -y nfd
sudo cp /usr/local/etc/ndn/nfd.conf.sample /usr/local/etc/ndn/nfd.conf


# # ndn C++ lib
# # https://github.com/named-data/ndn-cxx/blob/master/docs/INSTALL.rst
# sudo aptitude install -y g++ pkg-config python3-minimal libboost-all-dev libssl-dev libsqlite3-dev
# sudo apt install -y doxygen graphviz python3-pip
# sudo pip3 install sphinx sphinxcontrib-doxylink
# git clone https://github.com/named-data/ndn-cxx.git
# cd ndn-cxx
# ./waf configure --with-examples  # on CentOS, add --without-pch
# ./waf
# sudo ./waf install
# sudo ldconfig  # on Linux only
# cd ..


# # ndn tool
# # https://github.com/named-data/ndn-tools/blob/master/INSTALL.md
# sudo aptitude install -y libpcap-dev
# git clone https://github.com/named-data/ndn-tools.git
# cd ndn-tools
# ./waf configure
# ./waf
# sudo ./waf install
# cd ..
