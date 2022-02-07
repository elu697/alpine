# set -Cux

# for i in {2..20}; do
#     ssh icnl_lrd_vm$i pwd
# done
export DEBIAN_FRONTEND=noninteractive
for i in {2..20}; do
    if test $((i % 10)) -eq 0; then
        (ssh icnl_lrd_vm$i 'cd /home/docker/alpine/nln && git pull')
    else
        (ssh icnl_lrd_vm$i 'cd /home/docker/alpine/nln && git pull')&
    fi
done


# for i in {2..20}; do
#     if test $((i % 10)) -eq 0; then
#         (ssh icnl_lrd_vm$i 'cd /home/docker/alpine && git pull && cd setup/ndn && bash ndn_setup.sh')
#     else
#         (ssh icnl_lrd_vm$i 'cd /home/docker/alpine && git pull && cd setup/ndn && bash ndn_setup.sh')&
#     fi
# done

# for i in {2..20}; do
#     if test $((i % 10)) -eq 0; then
#         (ssh icnl_lrd_vm$i 'cd /home/docker/alpine && git pull && cd nln && bash package.sh')
#     else
#         (ssh icnl_lrd_vm$i 'cd /home/docker/alpine && git pull && cd nln && bash package.sh')&
#     fi
# done


# (ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm3 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm4 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm5 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm6 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm7 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm8 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm9 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm10 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm11 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm12 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm13 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm14 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm15 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm16 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm17 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm18 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm19 "cd /home/docker/alpine/nln && bash run.sh")&
# (ssh icnl_lrd_vm20 "cd /home/docker/alpine/nln && bash run.sh")

