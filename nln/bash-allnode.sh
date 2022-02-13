# set -Cux

# for i in {0..45}; do
#     ssh icnl_lrd_vm$i pwd
# done

(ssh icnl_lrd_host 'cd alpine/setup/docker && docker-compose restart')

# export DEBIAN_FRONTEND=noninteractive
# for i in {2..45}; do
#     if test $i -eq 30; then
#         (ssh icnl_lrd_vm$i 'cd /home/docker/alpine/nln && git pull')
#     else
#         (ssh icnl_lrd_vm$i 'cd /home/docker/alpine/nln && git pull')&
#         sleep 1
#     fi
# done

# for i in {21..45}; do
#     if test $((i%10)) -eq 0; then
#         (ssh icnl_lrd_vm$i 'cd /home/docker/alpine && git pull && cd setup/ndn && bash ndn_setup.sh')&
#     else
#         (ssh icnl_lrd_vm$i 'cd /home/docker/alpine && git pull && cd setup/ndn && bash ndn_setup.sh')&
#         sleep 1
#     fi
# done

# for i in {2..20}; do
#     if test $i -eq 20; then
#         (ssh icnl_lrd_vm$i 'cd /home/docker/alpine && git pull && cd nln/core && mvn clean')
#     else
#         (ssh icnl_lrd_vm$i 'cd /home/docker/alpine && git pull && cd nln/core && mvn clean')&
#         sleep 1
#     fi

# done

for i in {2..45}; do
    if test $i -eq 20; then
        (ssh icnl_lrd_vm$i 'cd /home/docker/alpine && git pull && cd nln && bash package.sh')&
    else
        (ssh icnl_lrd_vm$i 'cd /home/docker/alpine && git pull && cd nln && bash package.sh')&
        sleep 1
    fi
done



# 172.20.0.2 ~ 172.20.0.20, 172.20.0.NUM(vm{NUM})
# (ssh icnl_lrd_vm20 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm19 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm18 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm17 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm16 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm15 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm14 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm13 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm12 "cd /home/docker/alpine/nln && bash run.sh")&
# sleep 1
# (ssh icnl_lrd_vm11 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm10 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm9 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm8 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm7 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm6 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm5 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm4 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data1")&
# (ssh icnl_lrd_vm3 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data2")&
# (ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.3 172.20.0.4")&
# sleep 1
# (ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh consumer /model/A")&
# sleep 1

exit
