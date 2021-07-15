
sudo cefroute add ccnx:/test udp 127.0.0.1
sleep 5
cefputfile ccnx:/test/lg.txt -f cefore-0.8.3.zip
sleep 5
cefgetfile ccnx:/test/lg.txt
