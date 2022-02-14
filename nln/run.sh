set -Ceuxo pipefail
export DEBIAN_FRONTEND=noninteractive
PASSWORD=password
echo $PASSWORD | sudo -S ls

sudo nfd-start &

cd core
sudo chown docker:docker target/core-1.0.jar
java -jar target/core-1.0.jar $@
cd ..
exit
