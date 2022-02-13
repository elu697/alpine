export DEBIAN_FRONTEND=noninteractive
(ssh icnl_lrd_host 'cd alpine/setup/docker && docker-compose restart')


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
(ssh icnl_lrd_vm6 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data1")&
(ssh icnl_lrd_vm5 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data2")&
(ssh icnl_lrd_vm4 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data3")&
(ssh icnl_lrd_vm3 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data4")&
sleep 1
(ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.3 172.20.0.4 172.20.0.5 172.20.0.6")&
sleep 1
(ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh consumer /model/A")&
