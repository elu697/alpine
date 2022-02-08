set -Ceuxo pipefail

PASSWORD=password
echo $PASSWORD | sudo -S ls
sudo nfd-start

cd core
java -jar target/core-1.0.jar $@
cd ..
