# TURN(coturn) Redis

## 1. Installation
https://www.digitalocean.com/community/tutorials/how-to-install-and-secure-redis-on-centos-8
```
$ sudo dnf install redis
```
## 2. Configuration
```
$ sudo vi /etc/redis.conf
-----------------------------------------
bind 0.0.0.0å
port 6379
requirepass skychatuc
-----------------------------------------
```
```
$ redis-cli -a skychatucrw ping
```

## 3. Management
```
$ redis-cli -u redis://p%40ssw0rd@redis-16379.hosted.com:16379/0 ping
```
