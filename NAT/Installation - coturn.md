TURN(coturn) Installation

https://github.com/coturn/coturn
https://github.com/coturn/coturn/blob/master/INSTALL

0. Download & Install Prerequisite
* Install Database
0.1. SQLite
$ sudo yum install sqlite

Or
0.2. Redis
https://www.digitalocean.com/community/tutorials/how-to-install-and-secure-redis-on-centos-8
$ sudo dnf install redis

0.2.1. Redis Configuration

$ sudo vi /etc/redis.conf
	:
bind 127.0.0.1
port 6379    # default
	:
supervised systemd
	:
databases 16 # 0 ~ 15
	:
requirepass 2}U5h4K3DHVZb+ce

$ redis-cli
127.0.0.1:6379> auth 2}U5h4K3DHVZb+ce
127.0.0.1:6379> set key1 10
127.0.0.1:6379> get key 1

1. Install turn server
$ yum search coturn
$ sudo yum install coturn

2. Turn Server Configuration
$ sudo vi /etc/coturn/turnserver.conf
	:
listening-port=3478
tls-listening-port=5349
	:
fingerprint
lt-cred-mech
	:
server-name=voip
realm=skychat.com
	:
user=username3:password3
	:
redis-userdb="ip=localhost dbname=2 password=2}U5h4K3DHVZb+ce port=6379 connect_timeout=60"

user=username:password
   :
# listening-port=3478  # default TCP & UDP
   :
tls-listening-port=5349
   :
external-ip=64.141.83.122/10.89.89.60
   :
min-port=10000
max-port=25000
   :
# use-auth-secret
# static-auth-secret=E2"*U8&(m@AAZ6AB
   :
redis-userdb="ip=127.0.0.1 dbname=2 connect_timeout=30"

realm=voip.skychat.com
   :
no-udp
   :
cert=/etc/pki/coturn/public/turn_server_cert.pem
pkey=/etc/pki/coturn/private/turn_server_pkey.pem
   :
no-cli

3. Configure Firewall
$ sudo firewall-cmd --zone=public --permanent --add-port=5349/tcp
$ sudo firewall-cmd --zone=public --permanent --add-port=5349/udp
$ sudo firewall-cmd --zone=public --permanent --add-port=10000-20000/udp

$ sudo firewall-cmd –reload
$ sudo firewall-cmd --zone=public --list-ports

4. Start Turn Server
$ sudo turnserver -L 60.60.80.91 -o -a -b turnserver.conf -f -r turn.informaticar.net
or
$ sudo turnserver -L 60.60.80.91:5349 -o -a -b turnserver.conf -f -r turn.informaticar.net:5349
$ turnserver -v -r  127.1.1:2222 -a -b turnuserdb.conf -c turnserver.conf -u custom-username -r 127.1.1:2222 -p custom-password
$ sudo turnserver -L 64.141.83.122:5349 -o -a -b turnserver.conf -f -r voip.skychat.com:5349

5. Permanently run TURN server instance
$ nohup turnserver -v -r  ip:port -a -b turnuserdb.conf -c turnserver.conf -u turn-username -r ip:port -p turn-password &
   :
$ nohup TURN-execution-command &

--------------------------------------------------------------------------
/etc/coturn/turnserver.conf
--------------------------------------
1. turnserver
2. turnadmin
3. turnutils
- turnutils_uclient
- turnutils_peer
- turnutils_stunclient
- turnutils_rfc5769check

$ turnserver
$ turnadmin -a -u june -p Hello123 -r June
$ sudo tail -f /var/log/coturn/turnserver.log
