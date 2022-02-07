set -Ceuxo pipefail

# for i in {2..20}; do
#     ssh icnl_lrd_vm$i pwd
# done

for i in {2..20}; do
    (ssh icnl_lrd_vm$i "cd /home/docker/alpine && git pull && cd setup/ndn && ./ndn_setup.sh")&
    # (ssh icnl_lrd_vm$i "cd /home/docker/alpine/nln && git pull && ./package.sh")&
done

# (ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && git pull && ./package.sh")&
# (ssh icnl_lrd_vm3 "cd /home/docker/alpine/nln && git pull && ./package.sh")&
# (sshpass -p password ssh -l docker 172.20.0.6 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.7 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.8 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.9 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.10 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.11 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.12 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.13 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.14 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.15 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.16 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.17 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.18 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.19 cd alpine/nln && git pull &)&
# (sshpass -p password ssh -l docker 172.20.0.20 cd alpine/nln && git pull &)&

