set -Ceuxo pipefail
export DEBIAN_FRONTEND=noninteractive
PASSWORD=password
echo $PASSWORD | sudo -S ls
# sudo aptitude install -y maven
sudo nfd-start &
sleep 3
cd core
java -jar target/core-1.0.jar $@
cd ..
exit
