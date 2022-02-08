set -Cux
export DEBIAN_FRONTEND=noninteractive
cd core
mvn package
cd ..
exit
