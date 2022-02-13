export DEBIAN_FRONTEND=noninteractive
(ssh icnl_lrd_host 'cd alpine/setup/docker && docker-compose restart')


# 172.20.0.2 ~ 172.20.0.20, 172.20.0.NUM(vm{NUM})
(ssh icnl_lrd_vm22 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/1")&
(ssh icnl_lrd_vm21 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/2")&
(ssh icnl_lrd_vm20 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/3")&
(ssh icnl_lrd_vm19 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/4")&
(ssh icnl_lrd_vm18 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/5")&
(ssh icnl_lrd_vm17 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/6")&
(ssh icnl_lrd_vm16 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/7")&
(ssh icnl_lrd_vm15 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/8")&
(ssh icnl_lrd_vm14 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/9")&
(ssh icnl_lrd_vm13 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/10")&
(ssh icnl_lrd_vm12 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/11")&
(ssh icnl_lrd_vm11 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/12")&
(ssh icnl_lrd_vm10 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/13")&
(ssh icnl_lrd_vm9 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/14")&
(ssh icnl_lrd_vm8 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/15")&
(ssh icnl_lrd_vm7 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A /data/16")&
sleep 1
(ssh icnl_lrd_vm6 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.19 172.20.0.20 172.20.0.21 172.20.0.22")&
(ssh icnl_lrd_vm5 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.15 172.20.0.16 172.20.0.17 172.20.0.18")&
(ssh icnl_lrd_vm4 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.11 172.20.0.12 172.20.0.13 172.20.0.14")&
(ssh icnl_lrd_vm3 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.7 172.20.0.8 172.20.0.9 172.20.0.10")&
sleep 1
(ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.3 172.20.0.4 172.20.0.5 172.20.0.6")&
sleep 1
(ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh consumer /model/A")&
