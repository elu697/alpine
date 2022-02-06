set -Ceuxo pipefail

cd core
#mvn package
java -cp target/core-1.0.jar $@
cd ..
