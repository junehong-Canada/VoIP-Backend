RTPProxy Installation

https://www.rtpproxy.org/
https://github.com/sippy/rtpproxy
https://www.rtpproxy.org/doc/master/user_manual.html#MAKESRC
https://computingforgeeks.com/how-to-install-rtpproxy-from-source-in-centos-linux/
https://dopensource.com/2017/05/31/installing-configuring-rtpproxy/

0. Install pre-req’s
$ sudo yum -y update
$ sudo dnf group install “Development Tools”
1. Download source code, build, and install
$ git clone --recursive https://github.com/sippy/rtpproxy.git
$ cd rtpproxy
$ ./configure --enable-systemd
$ make
$ sudo make install
2. Configuration
- init.d and systemd: The system is the most relevant solution
2.1. init.d
1) Copy init.d script to /etc/rc.d/init.d directory and make it executable.
$ sudo cp rpm/rtpproxy.init /etc/rc.d/init.d/rtpproxy
$ sudo chmod +x /etc/rc.d/init.d/rtpproxy
2) Edit /etc/rc.d/init.d/rtpproxy file
$ sudo vi /etc/rc.d/init.d/rtpproxy
# processname: rtpproxy
# pidfile: /var/run/rtpproxy/rtpproxy.pid

prog=rtpproxy
rtpproxy=/usr/local/bin/$progsystemctl

user=rtpproxy
lockfile=/var/lock/subsys/$prog
pidfile=/var/run/rtpproxy/$prog.pid
 3) Add a user and group
$ sudo mkdir -p /var/run/rtpproxy
$ sudo groupadd -g 8002 rtpproxy
$ sudo useradd -u 8002 -g 8002 -d /var/run/rtpproxy -M -s /bin/false rtpproxy
$ sudo chown rtpproxy:rtpproxy -R /var/run/rtpproxy/
Or
$ sudo mkdir -p /var/run/rtpproxy
$ sudo groupadd rtpproxy
$ sudo useradd -g rtpproxy -d /var/run/rtpproxy -M -s /bin/false rtpproxy
$ sudo chown rtpproxy:rtpproxy -R /var/run/rtpproxy
4) Check the location of RTPProxy executable file
$ which rtpproxy
/usr/local/bin/rtpproxy
5) Edit OPTIONS and firewall. And kamailio.cfg
$ sudo vi /etc/sysconfig/rtpproxy
OPTIONS=" -l 192.168.1.60 -s unix:/var/run/rtpproxy.sock"
// Rtpproxy will listen on ip: 192.168.1.60, control socket being unix:/var/run/rtpproxy.sock.
CONTROL_SOCK=udp:127.0.0.1:7722
// To make it listen on an UDP socket.
$ sudo vi /etc/sysconfig/rtpproxy
OPTIONS=" -F -l 10.89.89.60 -A 64.141.83.122 -m 20000 -M 30000 -s udp:10.89.89.60:7722 -d DBUG:LOG_LOCAL5"

$ sudo firewall-cmd --zone=public --permanent --add-port=7722/udp
$ sudo firewall-cmd --zone=public --permanent --add-port=35000-65000/udp
$ sudo firewall-cmd --reload
$ sudo firewall-cmd --zone=public --list-ports

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

$ sudo source /etc/profile & source ~/.bashrc

$ sudo chkconfig rtpproxy on
2.2. systemd
2.2.1. Create rtpproxy.socket system unit file
- /etc/systemd/system/sockets.target.wants/rtpproxy.socket (better to create it in /lib/systemd/system/sockets.target.wants/ and symlink it)
[Socket]
ListenStream=/var/run/rtpproxy/rtpproxy.sock
SocketUser=rtpproxy
SocketGroup=rtpproxy
SocketMode=755
ExecStartPost=-/bin/chown rtpproxy:rtpproxy /var/run/rtpproxy

[Install]
WantedBy=sockets.target
2.2.2. Set a parameter in startup row of rtpproxy.service unit
[Unit]
Description=RTPProxy media server
After=network.target
Requires=network.target

[Service]
Type=simple
PIDFile=/var/run/rtpproxy/rtpproxy.pid
Environment='OPTIONS= -f -L 4096 -l 0.0.0.0 -m 10000 -M 20000 -d INFO:LOG_LOCAL5'

Restart=always
RestartSec=5

ExecStartPre=-/bin/mkdir /var/run/rtpproxy
ExecStartPre=-/bin/chown rtpproxy:rtpproxy /var/run/rtpproxy

ExecStart=/usr/local/bin/rtpproxy -p /var/run/rtpproxy/rtpproxy.pid -s unix:/var/run/rtpproxy/rtpproxy.sock -u rtpproxy rtpproxy -n unix:/var/run/rtpproxy/rtpproxy_timeout.sock $OPTIONS

ExecStart=/usr/local/bin/rtpproxy -p /var/run/rtpproxy/rtpproxy.pid -s systemd: -u rtpproxy:rtpproxy -n unix:/var/run/rtpproxy/rtpproxy_timeout.sock -f -l 0.0.0.0 -m 10000 -M 20000 -d INFO:LOG_DAEMON

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
3. Reload systemd configuration and enable socket unit:
bash> sudo systemctl daemon-reload
bash> sudo systemctl enable rtpproxy.socket

bash> sudo systemctl start rtpproxy.socket
bash> sudo systemctl start rtpproxy.service
