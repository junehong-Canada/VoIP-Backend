# RTPProxy Installation

https://www.rtpproxy.org/ <br>
https://github.com/sippy/rtpproxy <br>
https://www.rtpproxy.org/doc/master/user_manual.html#MAKESRC <br>
https://computingforgeeks.com/how-to-install-rtpproxy-from-source-in-centos-linux/ <br>
https://dopensource.com/2017/05/31/installing-configuring-rtpproxy/ <br>

## 0. Prerequisite
```
$ sudo yum -y update
$ sudo dnf group install “Development Tools”
$ sudo yum install systemd-devel
$ sudo yum install centos-release-scl
$ sudo yum install devtoolset-9-gcc*
$ scl enable devtoolset-9 bash
```
## 1. Download source code, build, and install
```
$ git clone --recursive https://github.com/sippy/rtpproxy.git
$ cd rtpproxy
$ ./configure --enable-systemd
$ make
$ sudo make install
```
## 2. Configuration
### 1) Check the location of RTPProxy executable file
```
$ which rtpproxy
/usr/local/bin/rtpproxy
```
### 2) Create rtpproxy.service system unit file
- create it in /lib/systemd/system/rtpproxy.service and symlink it /etc/systemd/system/rtpproxy.service -> /lib/systemd/system/rtpproxy.service
```
[Unit]
Description=RTPProxy media server
After=network.target
Requires=network.target

[Service]
Type=simple
PIDFile=/var/run/rtpproxy/rtpproxy.pid
Environment='OPTIONS= -f -l 0.0.0.0 -m 35000 -M 65000 -d INFO:LOG_DAEMON'

ExecStartPre=-/bin/mkdir /var/run/rtpproxy
ExecStartPre=-/bin/chown rtpproxy:rtpproxy /var/run/rtpproxy

ExecStart=/usr/local/bin/rtpproxy -p /var/run/rtpproxy/rtpproxy.pid -s udp:10.89.89.61:7722 -u rtpproxy:rtpproxy $OPTIONS
ExecStop=/usr/bin/pkill -F /var/run/rtpproxy/rtpproxy.pid

ExecStopPost=-/bin/rm -R /var/run/rtpproxy

StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=rtpproxy
SyslogFacility=local5

TimeoutStartSec=10
TimeoutStopSec=10

[Install]
WantedBy=multi-user.target
```
### 3) Add a user and group
```
$ sudo mkdir -p /var/run/rtpproxy
$ sudo groupadd -g 8002 rtpproxy
$ sudo useradd -u 8002 -g 8002 -d /var/run/rtpproxy -M -s /bin/false rtpproxy
$ sudo chown rtpproxy:rtpproxy -R /var/run/rtpproxy/
```
Or
```
$ sudo mkdir -p /var/run/rtpproxy
$ sudo groupadd rtpproxy
$ sudo useradd -g rtpproxy -d /var/run/rtpproxy -M -s /bin/false rtpproxy
$ sudo chown rtpproxy:rtpproxy -R /var/run/rtpproxy
```

### 4) Reload systemd configuration and enable rtpproxy.service:
```
$ sudo systemctl daemon-reload
$ sudo systemctl start rtpproxy.service
$ sudo systemctl enable rtpproxy.service
```
### 5) Edit firewall.
```
$ sudo firewall-cmd --zone=public --permanent --add-port=7722/udp
$ sudo firewall-cmd --zone=public --permanent --add-port=35000-65000/udp
$ sudo firewall-cmd --reload
$ sudo firewall-cmd --zone=public --list-ports
```
### 6) Edit kamailio.cfg
```
$ sudo vi /etc/kamailio/kamailio.cfg
#!define WITH_NAT
modparam("rtpproxy", "rtpproxy_sock", "udp: 10.89.89.60:7722")
6) Reload and start rtpproxy
$ sudo systemctl daemon-reload
$ sudo /etc/init.d/rtpproxy start
7) To set to run at startup, type:
$ sudo chkconfig rtpproxy on
??? No need?
$ sudo cat << 'EOF' > /etc/profile.d/rtpproxy.sh
#!/bin/sh
#
# Set an alias for compiled version of rtpproxy

# Change if installed in custom location
rtpproxy_prog="/usr/local/bin/rtpproxy"

if [ -f "$rtpproxy_prog" ] && [ -x "$rtpproxy_prog" ]; then
    export PATH=$PATH:$rtpproxy_prog
fi
EOF
```

