set -Ceuxo pipefail
sudo apt install -y openjdk-11-jdk
sudo update-alternatives --config java
cd core
mvn package
cd ..
