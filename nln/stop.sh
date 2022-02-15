set -Ceuxo pipefail
export DEBIAN_FRONTEND=noninteractive
PASSWORD=password
echo $PASSWORD | sudo -S ls
# sudo aptitude install -y maven
# sudo systemctl restart docker
sudo nfd-stop &
exit
