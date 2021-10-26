# Kamailio Installation

https://computingforgeeks.com/how-to-install-latest-kamailio-sip-server-on-centos-linux/

## 1. Set SELinux to a permissive/Disabled mode
```
$ sudo setenforce 0
$ sudo sed -i 's/^SELINUX=.*/SELINUX=permissive/g' /etc/selinux/config
$ sestatus
SELinux status:                 enabled
SELinuxfs mount:                /sys/fs/selinux
SELinux root directory:         /etc/selinux
Loaded policy name:             targeted
Current mode:                   permissive
Mode from config file:          permissive
Policy MLS status:              enabled
Policy deny_unknown status:     allowed
Memory protection checking:     actual (secure)
Max kernel policy version:      33
```
## 2. Install MariaDB
```
$ sudo yum -y install mariadb-server
$ sudo systemctl enable --now mariadb
$ sudo mysql_secure_installation
```
## 3. Install Kamailio
```
$ sudo dnf -y install dnf-plugins-core
$ sudo dnf config-manager --add-repo https://rpm.kamailio.org/centos/kamailio.repo
$ sudo dnf install kamailio 
$ sudo yum install kamailio-presence kamailio-ldap kamailio-mysql kamailio-debuginfo kamailio-xmpp kamailio-unixodbc kamailio-utils kamailio-tls kamailio-outbound kamailio-gzcompress
```
## 4. Configure for Mariadb Database
```
$ sudo vi /etc/kamailio/kamctlrc
DBENGINE=MYSQL
DBHOST=localhost
## database read/write user
# DBRWUSER="kamailio"

## password for database read/write user
# DBRWPW="kamailiorw"

## database read only user
# DBROUSER="kamailioro"

## password for database read only user
# DBROPW="kamailioro"
```
## 5. Configure for Mariadb Database and etc
```
$ sudo vi /etc/kamailio/kamailio.cfg
#!define WITH_MYSQL
#!define WITH_AUTH
#!define WITH_USRLOCDB
#!define WITH_PRESENCE
#!define WITH_NAT
#!define WITH_ACCDB

# #!define WITH_TLS

#!define WITH_SENDPUSH

# #!define WITH_DEBUG
```
## 6. Start Mariadb Database, create database for accounts management, and start kamailio.
```
$ sudo systemctl start mariadb
$ kamdbctl create
$ sudo systemctl start kamailio
```
## 7. Account Management
* All users inforamtion are stored in table "kamailio.subscriber"
```
$ kamctl db show subscriber
```
* Add new user
```
$ kamctl add 100@skychat.com 100passwd
```
* Delete new user
```
$ kamctl rm 100@skychat.com
```

## 8. Add Firebase ID into accounts management table.
```
$ mysql -u -root -p
Enter password: *****
MariaDB [(none)]> use kamailio;
MariaDB [kamailio]> ALTER TABLE subscriber ADD fcmId varchar(255) NOT NULL AFTER ha1b;
MariaDB [kamailio]> ALTER TABLE subscriber CHANGE COLUMN fcmId fcm_id varchar(255) NOT NULL;
MariaDB [kamailio]> commit;
```
## 9. Firewall
```
$ sudo firewall-cmd --permanent --add-port=5060/udp
$ sudo firewall-cmd --reload
$ sudo firewall-cmd --list-ports
```

## 9. Etc
• Logging
```
$ journalctl -u kamailio.service -f
```
• Clear and Backup database
```
$ sudo systemctl stop mysql

$ sudo rm -rf /var/lib/mysql/*				<= clear database
$ sudo cp -r /var/lib/mysql/* /var/lib/mysql_back/*	<= backup database

$ sudo systemctl start mysql
```
• kamailio module list
```
 kamailio   - very fast and configurable SIP proxy
 kamailio-autheph-modules - authentication using ephemeral credentials module for Kamailio
 kamailio-berkeley-bin - Berkeley database module for Kamailio - helper program
 kamailio-berkeley-modules - Berkeley database module for Kamailio
 kamailio-carrierroute-modules - carrierroute module for Kamailio
 kamailio-cnxcc-modules - cnxcc modules for Kamailio
 kamailio-cpl-modules - CPL module (CPL interpreter engine) for Kamailio
 kamailio-dbg - very fast and configurable SIP proxy [debug symbols]
 kamailio-dnssec-modules - contains the dnssec module
 kamailio-erlang-modules - erlang modules for Kamailio
 kamailio-extra-modules - extra modules for Kamailio
 kamailio-geoip-modules - contains the geoip module
 kamailio-ims-modules - IMS module for Kamailio
 kamailio-java-modules - contains the app_java module
 kamailio-json-modules - Json parser and jsonrpc modules for Kamailio
 kamailio-kazoo-modules - kazoo modules for Kamailio
 kamailio-ldap-modules - LDAP modules for Kamailio
 kamailio-lua-modules - contains the app_lua module
 kamailio-memcached-modules - interface to memcached server
 kamailio-mono-modules - contains the app_mono module
 kamailio-mysql-modules - MySQL database connectivity module for Kamailio
 kamailio-outbound-modules - Outbound module for Kamailio
 kamailio-perl-modules - Perl extensions and database driver for Kamailio
 kamailio-postgres-modules - PostgreSQL database connectivity module for Kamailio
 kamailio-presence-modules - SIMPLE presence modules for Kamailio
 kamailio-purple-modules - Provides the purple module, a multi-protocol IM gateway
 kamailio-python-modules - contains the app_python module
 kamailio-radius-modules - RADIUS modules for Kamailio
 kamailio-redis-modules - Redis database connectivity module for Kamailio
 kamailio-sctp-modules - sctp module for Kamailio
 kamailio-snmpstats-modules - SNMP AgentX subagent module for Kamailio
 kamailio-sqlite-modules - SQLite database connectivity module for Kamailio
 kamailio-tls-modules - contains the TLS kamailio transport module
 kamailio-unixodbc-modules - unixODBC database connectivity module for Kamailio
 kamailio-utils-modules - Provides a set utility functions for Kamailio
 kamailio-websocket-modules - Websocket module for kamailio
 kamailio-xml-modules - XML based extensions for Kamailio's Management Interface
 kamailio-xmpp-modules - XMPP gateway module for Kamailio
```
