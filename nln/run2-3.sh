export DEBIAN_FRONTEND=noninteractive
(ssh icnl_lrd_host 'cd alpine/setup/docker && docker-compose restart')


# 172.20.0.2 ~ 172.20.0.20, 172.20.0.NUM(vm{NUM})
(ssh icnl_lrd_vm16 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data1")&
(ssh icnl_lrd_vm15 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data2")&
(ssh icnl_lrd_vm14 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data3")&
(ssh icnl_lrd_vm13 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data4")&
(ssh icnl_lrd_vm12 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data5")&
(ssh icnl_lrd_vm11 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data6")&
(ssh icnl_lrd_vm10 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data7")&
(ssh icnl_lrd_vm9 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data8")&
sleep 1
(ssh icnl_lrd_vm8 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.16 172.20.0.15")&
(ssh icnl_lrd_vm7 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.14 172.20.0.13")&
(ssh icnl_lrd_vm6 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.12 172.20.0.11")&
(ssh icnl_lrd_vm5 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.10 172.20.0.9")&
sleep 1
(ssh icnl_lrd_vm4 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.7 172.20.0.8")&
(ssh icnl_lrd_vm3 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.5 172.20.0.6")&
sleep 1
(ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.3 172.20.0.4")&
sleep 1
(ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh consumer /model/A")&
