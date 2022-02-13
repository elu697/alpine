# set -Cux

# for i in {0..45}; do
#     ssh icnl_lrd_vm$i pwd
# done

(ssh icnl_lrd_host 'cd alpine/setup/docker && docker-compose restart')
export DEBIAN_FRONTEND=noninteractive

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
        sleep 60
    else
        (ssh icnl_lrd_vm$i 'cd /home/docker/alpine && git pull && cd nln && bash package.sh')&
        sleep 1
    fi
done

exit
