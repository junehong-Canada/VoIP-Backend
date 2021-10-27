## How to build for running and deployment

1. Add Configuration for running
- Edit Run/Debug Configuration -> Add new... -> Gradle -> Name proxyserver, Run: bootRun

2. Add Configuration for deployment
- Go to Gradle tap -> proxyserver -> Tasks -> build -> bootJar, Run bootJar by double-clicking.
- proxyserver/build/libs/proxyserver-0.0.1-SNAPSHOT.jar

## How to deploy
1. Create voip_proxy.service system unit file
- create it in /lib/systemd/system/voip_proxy.service 
- and symlink it; /etc/systemd/system/voip_proxy.service -> /lib/systemd/system/voip_proxy.service
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
2. Create voip_proxy_assist.service system unit file
- create it in /lib/systemd/system/voip_proxy_assist.service 
- and symlink it; /etc/systemd/system/voip_proxy_assist.service -> /lib/systemd/system/voip_proxy_assist.service
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
WantedBy=multi-user.target```
