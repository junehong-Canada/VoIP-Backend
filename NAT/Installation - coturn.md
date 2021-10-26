# TURN(coturn) Installation

https://github.com/coturn/coturn <br>
https://github.com/coturn/coturn/blob/master/INSTALL

## 0. Prerequisite
### Install Database
#### 0.1. Redis Installation
https://www.digitalocean.com/community/tutorials/how-to-install-and-secure-redis-on-centos-8
```
$ sudo dnf install redis
```
#### 0.2. Redis Configuration
```
$ sudo vi /etc/redis.conf
	:
bind 127.0.0.1	# default
port 6379    # default
	:
supervised systemd
	:
databases 16 # default, 0 ~ 15
	:
requirepass Hello123
```
#### 0.3. Start the Redis service
```
$ sudo systemctl start redis.service
```
#### 0.4. Make the Redis to start on boot
```
$ sudo systemctl enable redis
```
#### 0.5. Test the Redis using redis-cli
```
$ redis-cli
127.0.0.1:6379> auth Hello123
127.0.0.1:6379> set key1 10
127.0.0.1:6379> get key1
```
## 1. Install turn server
```
$ yum search coturn
$ sudo yum install coturn
```
## 2. Turn Server Configuration
```
$ sudo vi /etc/coturn/turnserver.conf
	:
listening-port=3478
tls-listening-port=5349
	:
server-name=voip.skychat.com <=== change to domain name of instance
	:
redis-userdb="ip=localhost dbname=2 password=Hello123 port=6379 connect_timeout=60"
   	:
external-ip=64.141.83.122 # <=== change to Public IP of instance
	:
min-port=10000
max-port=20000
   	:
```
## 3. Configure Firewall
```
$ sudo firewall-cmd --zone=public --permanent --add-port=5349/tcp
$ sudo firewall-cmd --zone=public --permanent --add-port=5349/udp
$ sudo firewall-cmd --zone=public --permanent --add-port=10000-20000/udp

$ sudo firewall-cmd --reload
$ sudo firewall-cmd --zone=public --list-ports
```
## 4. Start Turn Server and make it to start on boot
```
$ sudo systemctl start coturn
```
```
$ sudo systemctl enable coturn
```
## 5. Logging & Etc
```
$ sudo tail -f /var/log/coturn/turnserver.log
```
