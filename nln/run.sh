set -Ceuxo pipefail
export DEBIAN_FRONTEND=noninteractive
PASSWORD=password
echo $PASSWORD | sudo -S ls
sudo nfd-stop
sudo nfd-start

cd core
java -jar target/core-1.0.jar $@
cd ..
exit
