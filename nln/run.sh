set -Ceuxo pipefail

cd core
java -jar target/core-1.0.jar $@
cd ..
