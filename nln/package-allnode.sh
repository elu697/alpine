set -Ceuxo pipefail

# {
#     sshpass -p password ssh docker@172.20.0.2 echo "" && echo A
# }&
for i in {2..20}; do
    (sshpass -p password ssh -l docker 172.20.0.$i cd alpine/nln && git pull && ./package.sh)&
done

