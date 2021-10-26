Kamailio Installation

https://computingforgeeks.com/how-to-install-latest-kamailio-sip-server-on-centos-linux/

1. Set SELinux to a permissive/Disabled mode
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

2. Install MariaDB
$ sudo yum -y install mariadb-server
$ sudo systemctl enable --now mariadb
$ sudo mysql_secure_installation

3. Install Kamailio
$ sudo dnf -y install dnf-plugins-core
$ sudo dnf config-manager –add-repo https://rpm.kamailio.org/centos/kamailio.repo
$ sudo yum install vim kamailio kamailio-presence kamailio-ldap kamailio-mysql kamailio-debuginfo kamailio-xmpp kamailio-unixodbc kamailio-utils kamailio-tls kamailio-outbound kamailio-gzcompress
$ kamailio -version
version: kamailio 5.5.0
Or
$ sudo dnf -y install dnf-plugins-core
$ sudo dnf config-manager --add-repo https://rpm.kamailio.org/centos/kamailio.repo
$ sudo dnf install kamailioa

4. Configure Mariadb Database
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
$ sudo vi /etc/kamailio/kamailio.cfg
#!define WITH_MYSQL
#!define WITH_AUTH
#!define WITH_USRLOCDB
#!define WITH_PRESENCE
#!define WITH_NAT
#!define WITH_ACCDB
$ sudo service mysql start
$ kamdbctl create
$ sudo firewall-cmd --permanent --add-port=5060/udp
$ sudo firewall-cmd --reload
$ sudo firewall-cmd --list-ports

• Clear and Backup database
$ sudo systemctl stop mysql

$ sudo rm -rf /var/lib/mysql/*				<= clear database
$ sudo cp -r /var/lib/mysql/* /var/lib/mysql_back/*	<= backup database

$ sudo systemctl start mysql

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
