export DEBIAN_FRONTEND=noninteractive
(ssh icnl_lrd_host 'cd alpine/setup/docker && docker-compose restart')


# 172.20.0.2 ~ 172.20.0.20, 172.20.0.NUM(vm{NUM})
(ssh icnl_lrd_vm41 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data27")&
(ssh icnl_lrd_vm40 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data26")&
(ssh icnl_lrd_vm39 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data25")&
(ssh icnl_lrd_vm38 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data24")&
(ssh icnl_lrd_vm37 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data23")&
(ssh icnl_lrd_vm36 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data22")&
(ssh icnl_lrd_vm35 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data21")&
(ssh icnl_lrd_vm34 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data20")&
(ssh icnl_lrd_vm33 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data19")&
(ssh icnl_lrd_vm32 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data18")&
(ssh icnl_lrd_vm31 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data17")&
(ssh icnl_lrd_vm30 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data16")&
(ssh icnl_lrd_vm29 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data15")&
(ssh icnl_lrd_vm28 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data14")&
(ssh icnl_lrd_vm27 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data13")&
(ssh icnl_lrd_vm26 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data12")&
(ssh icnl_lrd_vm25 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data11")&
(ssh icnl_lrd_vm24 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data10")&
(ssh icnl_lrd_vm23 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data9")&
(ssh icnl_lrd_vm22 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data8")&
(ssh icnl_lrd_vm21 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data7")&
(ssh icnl_lrd_vm20 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data6")&
(ssh icnl_lrd_vm19 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data5")&
(ssh icnl_lrd_vm18 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data4")&
(ssh icnl_lrd_vm17 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data3")&
(ssh icnl_lrd_vm16 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data2")&
(ssh icnl_lrd_vm15 "cd /home/docker/alpine/nln && bash run.sh router2 /model/A /data1")&
sleep 1
(ssh icnl_lrd_vm14 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.39 172.20.0.40 172.20.0.41")&
(ssh icnl_lrd_vm13 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.36 172.20.0.37 172.20.0.38")&
(ssh icnl_lrd_vm12 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.33 172.20.0.34 172.20.0.35")&
(ssh icnl_lrd_vm11 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.30 172.20.0.31 172.20.0.32")&
(ssh icnl_lrd_vm10 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.27 172.20.0.28 172.20.0.29")&
(ssh icnl_lrd_vm9 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.24 172.20.0.25 172.20.0.26")&
(ssh icnl_lrd_vm8 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.21 172.20.0.22 172.20.0.23")&
(ssh icnl_lrd_vm7 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.18 172.20.0.19 172.20.0.20")&
(ssh icnl_lrd_vm6 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.15 172.20.0.16 172.20.0.17")&
sleep 1
(ssh icnl_lrd_vm5 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.12 172.20.0.13 172.20.0.14")&
(ssh icnl_lrd_vm4 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.9 172.20.0.10 172.20.0.11")&
(ssh icnl_lrd_vm3 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.6 172.20.0.7 172.20.0.8")&
sleep 1
(ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh router1 /model/A 172.20.0.3 172.20.0.4 172.20.0.5")&
sleep 1
(ssh icnl_lrd_vm2 "cd /home/docker/alpine/nln && bash run.sh consumer /model/A")&
