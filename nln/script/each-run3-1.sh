export DEBIAN_FRONTEND=noninteractive
(ssh icnl_lrd_host 'cd alpine/setup/docker && docker-compose restart')


# 172.20.0.2 ~ 172.20.0.20, 172.20.0.NUM(vm{NUM})
(ssh icnl_lrd_vm5 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data3")&
(ssh icnl_lrd_vm4 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data2")&
(ssh icnl_lrd_vm3 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data1")&
sleep 1
(ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.3 172.20.0.4 172.20.0.5")&
sleep 1
(ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh consumer /model/A")&
