## How to build for running and deployment

1) Add Configuration for running
- Edit Run/Debug Configuration -> Add new... -> Gradle -> Name proxyserver, Run: bootRun

2) Add Configuration for deployment
- Go to Gradle tap -> proxyserver -> Tasks -> build -> bootJar, Run bootJar by double-clicking.
- proxyserver/build/libs/proxyserver-0.0.1-SNAPSHOT.jar

## How to deploy
### 1. voip_proxy.service 
1) Create voip_proxy.service system unit file
- create it in ~/voip_proxy/voip_proxy.service
- and symlink it; /etc/systemd/system/voip_proxy.service -> ~/voip_proxy/voip_proxy.service
```
[Unit]
Description=SKY Chat VoIP Proxy
After=syslog.target
After=network.target

[Service]
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=dev /home/june/voip_proxy/proxyserver-0.0.1-SNAPSHOT.jar
Restart=always
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=voip_proxy

[Install]
WantedBy=multi-user.target
```
2) Copy proxyserver-0.0.1-SNAPSHOT.jar, skychat.p12, and egovoip-firebase-adminsdk-t09m9-7ebcf375e4.json into ~/voip_proxy.
```
[june@sig voip_proxy]$ ls -al
total 69776
drwxrwxr-x. 2 june june      177 Oct 27 11:18 .
drwx------. 6 june june     4096 Oct 27 11:18 ..
-rw-r--r--. 1 june june     2322 Oct  6 16:24 egovoip-firebase-adminsdk-t09m9-7ebcf375e4.json
-rw-r--r--. 1 june june 71425698 Sep  2 14:57 proxyserver-0.0.1-SNAPSHOT.jar
-rw-rw-r--. 1 june june     4238 Jul 20 11:53 skychat.p12
-rw-------. 1 june june      350 Aug 30 11:41 voip_proxy_assist.service
-rw-rw-r--. 1 june june      326 Jul 20 15:13 voip_proxy.service
```

### 2. Create voip_proxy.service system unit file
- create it in ~/voip_proxy/voip_proxy_assist.service
- and symlink it; /etc/systemd/system/voip_proxy_assist.service -> ~/voip_proxy/voip_proxy_assist.service
```
[Unit]
Description=SKY Chat VoIP Proxy Assistant
After=syslog.target
After=network.target

[Service]
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=dev_assist /home/june/voip_proxy/proxyserver-0.0.1-SNAPSHOT.jar
Restart=always
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=voip_proxy_assist

[Install]
WantedBy=multi-user.target
```
