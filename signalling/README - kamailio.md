# Kamailio

* Account Management
- Add new user
```
$ kamctl add 100@skychat.com 100passwd
```
=> All users inforamtion are stored in table "kamailio.subscriber".
```
$ kamctl db show subscriber
$ kamctl db show presentity
```
```
$ kamctl rm 100@skychat.com
```
```
/etc/my.cnf.d/mariadb-server.cnf
bind-address=0.0.0.0
```

* Ports
```
# open port
sudo firewall-cmd --permanent --add-port=5060/udp
# reload
sudo firewall-cmd --reload
# list open ports
sudo firewall-cmd --list-ports
```
* The Kamailio Logging
```
$ journalctl -u kamailio.service -f
```

### Features of Kamailio
Kamailio’s main advantages for use alongside Media server like Asterisk are:
```
Kamailio can handle over 5000 call setups per second.
Can serve up to 300,000 active subscribers with just a 4GB Ram System.
Clustering can easily be realized by adding more Kamailio servers
Kamailio can function as:
● Registrar server
● Location server
● Proxy server
● SIP Application server
● Redirect server

Other Features of Kamailio are:
NAT traversal support for SIP and RTP traffic
Load balancing with many distribution algorithms and
failover support
Provides flexible least cost routing
Easy to realize routing failover
Support both IPv4 and IPv6
SCTP multi-homing and multi-streaming
Communication can be over UDP, TCP, TLS, and SCTP
Digest SIP User authentication
Provides event-based accounting
Data storage can be to database, Radius or Diameter
Extensible Java, Python, Lua and Perl Programming interface
TLS support for SIP signaling and transparent handling of SRTP for secure audio
```
