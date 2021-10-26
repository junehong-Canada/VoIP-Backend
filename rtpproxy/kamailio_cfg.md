```
/etc/kamailio/kamailio.cfg
loadmodule "rtpproxy.so"
modparam("rtpproxy", "rtpproxy_sock", "udp:1xx.xx.xx.xx4:12221") # CUSTOMIZE ME
-------------------------------------------------------
#!define WITH_NAT

#!ifdef WITH_NAT
loadmodule "nathelper.so"	<=== check
loadmodule "rtpproxy.so"
#!endif

#!ifdef WITH_NAT
# ----- rtpproxy params -----
modparam("rtpproxy", "rtpproxy_sock", "udp:10.1.2.114:7722")
===============================================================================
rtpproxy -F -l 1xx.xx.xx.xx4 -s udp:1xx.xx.xx.xx4:12221 -d DBUG:LOG_LOCAL5
rtpproxy -A PUBLICIP -F -l PRIVATEIP -m 20000 -M 30000 -s udp:*:7722 -d INFO

* /etc/sysconfig/rtpproxy
local_ip=your.local.i.p
external_ip=your.external.i.p
echo 'OPTIONS="-F -s udp:127.0.0.1:7722 -l $local_ip -A $external_ip -m 10000 -M 20000 -d DBUG:LOG_LOCAL0"' > /etc/sysconfig/rtpproxy

$ sudo vi /etc/sysconfig/rtpproxy
OPTIONS=" -l 192.168.1.60 -s unix:/var/run/rtpproxy.sock"
CONTROL_SOCK=udp:127.0.0.1:7722
-------------------------------------------------------
OPTIONS ="-F -l $local_ip -A $external_ip -s udp:127.0.0.1:7722 -m 10000 -M 20000 -d DBUG:LOG_LOCAL5"
=======================================================

* OPTIONS
$ sudo vi /etc/sysconfig/rtpproxy
OPTIONS=" -l 192.168.1.60 -s unix:/var/run/rtpproxy.sock"
// Rtpproxy will listen on ip: 192.168.1.60 , control socket being unix:/var/run/rtpproxy.sock.
CONTROL_SOCK=udp:127.0.0.1:7722
// To make it listen on an UDP socket.
------------------------------------------------------------------
OPTIONS=" -F -l 10.1.2.114 -s udp:*:7722 -d DBUG:LOG_LOCAL5"

-F
By default the rtpproxy will warn user if running as superuser (UID 0) in local control mode and refuse to run in remote control mode at all.
This switch removes the check.

-l addr1[/addr2]
IPv4 listen IP address(es). You can specify either one or two addresses.
If two addresses are specified, the rtpproxy will work in bridging mode.

-A advaddr1[/advaddr2]
Set advertised address of rtpproxy. Useful if the rtpproxy is behind a NAT firewall.
(Amazon EC2) When the rtpproxy receives a session request from a SIP controller it will return the IP address(es) specified by the -A option.

-s ctrl_socket
This parameter configures rtpproxy control socket.
The control socket is used by the call controller for the purpose of creating, modifying, and deleting RTP sessions.
The control socket can also be used to fetch stats from the rtpproxy process, or about specific media sessions.
Format of ctrl_socket is <type>:<socket>. Following types are supported:

   udp: Create UDP control socket. In this mode rtpproxy will listen on a UDP socket for control messages from the call controlle.
      Example: -s udp:127.0.0.1:9000
      IP address can be '*' in which case rtpproxy will listen on all local interfaces. If port is omitted then port 22222 will be used.
      Note
         rtpproxy control protocol has no built-in security mechanisms.
         Make sure that you protect the listening IP and port properly when using rtpproxy with UDP control socket.

   udp6: Create IPv6 UDP control socket. In this mode rtpproxy will listen on UDP/IPv6 for control messages from the SIP Controller.
      Example: -s udp6:::1:9000

   tcp: Create IPv4 TCP control socket. In this mode rtpproxy will listen on TCP/IPv4 for control messages from the SIP Controller.
      Example: -s tcp:192.168.0.1:9001

   tcp6: Create IPv6 TCP control socket. In this mode rtpproxy will listen on TCP/IPv6 for control messages from the SIP Controller.
      Example: -s tcp6:::1:9002

   unix: Create UNIX domain socket in a datagram-like mode for control interface.
      In this mode the SIP Controller and rtpproxy must be running on the same host.
      This is traditional mode when rtpproxy would close connection after processing each command and sending reply.
      Example: -s unix:/var/run/rtpproxy.sock

   cunix: Create UNIX domain socket for control interface.
      In this mode the SIP Controller and rtpproxy must be running on the same host.
      Similar to unix: above, but the rtpproxy would keep control socket open and accept multiple commands via the same connection.
      Example: -s cunix:/var/run/rtpproxy.sock

   Default value is unix:/var/run/rtpproxy.sock.

-m min_port
Set lower limit on UDP ports range that the rtpproxy uses for RTP/RTCP sessions to min_port. Default is 35000.

-M max_port
Set upper limit on UDP ports range that the rtpproxy uses for RTP/RTCP sessions to max_port. Default is 65000.

-d log_level[:log_facility]
Configures the verbosity level of the log output.
Possible log_level values in the order from the most verbose to the least verbose are: DBUG, INFO, WARN, ERR and CRIT.
------------------------------------------------------------------

OPTIONS
The following command-line parameters are supported:

-?
Show summary of options.

-2
Send every RTP packet twice in sessions that use low-bitrate codecs. Only packets that are smaller than 128 bytes will be sent twice. This option can improve audio quality on lossy links.

-f
rtpproxy will stay in foreground mode if this option is set.

-V
Show version of program.

-v
Show supported rtpp command protocol revisions.

-l addr1[/addr2]
IPv4 listen IP address(es). You can specify either one or two addresses. If two addresses are specified, the rtpproxy will work in bridging mode.

-6 addr1[/addr2]
IPv6 listen IP address(es). You can specify either one or two addresses. If two addresses are specified, the rtpproxy will work in bridging mode.

-s ctrl_socket
This parameter configures rtpproxy control socket. The control socket is used by the call controller for the purpose of creating, modifying, and deleting RTP sessions. The control socket can also be used to fetch stats from the rtpproxy process, or about specific media sessions. Format of ctrl_socket is <type>:<socket>. Following types are supported:

udp: Create UDP control socket. In this mode rtpproxy will listen on a UDP socket for control messages from the call controlle.

Example: -s udp:127.0.0.1:9000

IP address can be '*' in which case rtpproxy will listen on all local interfaces. If port is omitted then port 22222 will be used.

Note
rtpproxy control protocol has no built-in security mechanisms. Make sure that you protect the listening IP and port properly when using rtpproxy with UDP control socket.

udp6: Create IPv6 UDP control socket. In this mode rtpproxy will listen on UDP/IPv6 for control messages from the SIP Controller.

Example: -s udp6:::1:9000

tcp: Create IPv4 TCP control socket. In this mode rtpproxy will listen on TCP/IPv4 for control messages from the SIP Controller.

Example: -s tcp:192.168.0.1:9001

tcp6: Create IPv6 TCP control socket. In this mode rtpproxy will listen on TCP/IPv6 for control messages from the SIP Controller.

Example: -s tcp6:::1:9002

unix: Create UNIX domain socket in a datagram-like mode for control interface. In this mode the SIP Controller and rtpproxy must be running on the same host. This is traditional mode when rtpproxy would close connection after processing each command and sending reply.

Example: -s unix:/var/run/rtpproxy.sock

cunix: Create UNIX domain socket for control interface. In this mode the SIP Controller and rtpproxy must be running on the same host. Similar to unix: above, but the rtpproxy would keep control socket open and accept multiple commands via the same connection.

Example: -s cunix:/var/run/rtpproxy.sock

Default value is unix:/var/run/rtpproxy.sock.

-t tos
Set ToS (Type of Service) in the outgoing IP header. Default value is 0xB8. Setting this parameter to -1 disables setting ToS resulting in operating system default ToS being used instead.

-r rec_dir
Directory to write recorded RTP sessions.

-S spool_dir
Spool directory for recording of RTP streams. When the session is stopped, the recording will be moved from the spool directory to the rec_dir directory as specified by the -r option.

-R
Prevent rtpproxy from recording RTCP when recording RTP. rtpproxy records RTCP by default when RTP recording is enabled.

-p pid_file
This parameter configures the name of the file where PID of running rtpproxy will be stored. Default is /var/run/rtpproxy.pid.

-T max_ttl
Specify the RTP inactivity timer. Defaults to 60 seconds.

If the rtpproxy does not receive any RTP packets for more than max_ttl it will then delete the session.

-L nofile_limit
Set the maximum number of open file descriptors per process. The default maximum is set by the operating system, and can be overridden using the -L flag.

The rtpproxy requires four file descriptors per session to ensure that it can reliably identify where each stream is coming from in a NAT firewall scenario.

-A advaddr1[/advaddr2]
Set advertised address of rtpproxy. Useful if the rtpproxy is behind a NAT firewall. (Amazon EC2) When the rtpproxy receives a session request from a SIP controller it will return the IP address(es) specified by the -A option.

-m min_port
Set lower limit on UDP ports range that the rtpproxy uses for RTP/RTCP sessions to min_port. Default is 35000.

-M max_port
Set upper limit on UDP ports range that the rtpproxy uses for RTP/RTCP sessions to max_port. Default is 65000.

-u uname[:gname]
Switch rtpproxy to UID identified by the uname and optional GID identified by gname when proxy is up and running.

-w sock_mode
Set access mode for the controlling UNIX-socket (if used). Only applies if rtpproxy runs under a different GID using -u option.

-F
By default the rtpproxy will warn user if running as superuser (UID 0) in local control mode and refuse to run in remote control mode at all. This switch removes the check.

-i
Enable independent RTP activity timeout mode. By default, a timeout (which results in automatic destruction of the session) can only occur if no RTP packets are received on any of the session's ports. This option, if set, varies that behaviour, such that a timeout will occur if packets are still being received on one port but not the other. The option should be used with caution since in some cases it's perfectly fine to have packets coming from only one side of conversation (i.e. when the second party has muted its audio).

-n timeout_socket
This parameter specifies permitted notification sockets only. The listening socket must be created by another application, preferably before starting rtpproxy.

Timeout notifications must be enabled by the SIP controller when setting up the session. The SIP Controller must specify the timeout_socket, and a notify_tag, which is expected to be an arbitrary string that can be used by the SIP controller to identify which session a received time out notification relates to.

If a SIP Controller specifies a notification socket for a session, and that socket is not specified using the -n flag, the rtpproxy will not send a notification, and will not produce an error. It will ignore the notification request.

Format of timeout_socket is <type>:<socket>. Following types are supported:

unix: Connect to UNIX domain socket for sending timeout notifications. In this mode B2BUA and rtpproxy must be running on the same host.

Example: -n unix:/var/run/rtpproxy_timeout.sock

tcp: Connect to a remote host using TCP/IP for sending timeout notifications. Format of the socket parameter in this case is <host>:<port>.

Example: -n tcp:10.20.30:12345

There is no default value, notifications are not sent and not permitted unless a value is specified explicitly. Multiple notification sockets can be provided by specifying the -n flag more than once.

-P
Record sessions using libpcap file format instead of non-standard ad-hoc format. The libpcap format, which is the de-facto standard for packet capturing software, has the advantage of being compatible with numerous third-party tools and utilities, such as tcpdump or Wireshark. The drawback of libpcap is slightly larger overhead (extra 12 bytes for every saved RTP packet for IPv4).

-a
Record all sessions going through the rtpproxy unconditionally. By default rtpproxy expects the SIP controller to enable recording on a per-session basis.

-d log_level[:log_facility]
Configures the verbosity level of the log output. Possible log_level values in the order from the most verbose to the least verbose are: DBUG, INFO, WARN, ERR and CRIT.

The optional log_facility parameter sets syslog(3) facility assigned to log messages.

Example: -d WARN:LOG_LOCAL5

The default level in foreground mode is is DBUG, in background - WARN and facility is LOG_DAEMON.

--force_asymmetric
Treat all RTP/RTCP sessions as "assymetric", i.e. disable any NAT traversal features unconditionally.
------------------------------------------------------------------
* Logging to a separate file
1. create a file and give it proper permissions.
	touch /var/log/rtpproxy.log
	chown rtpproxy:rtpproxy /var/log/rtpproxy.log
2. deinfe a log facility in rsyslog configuration file.
	vim /etc/rsyslog.d/50-default.conf
	local5.*        /var/log/rtpproxy.log

	$ systemctl restart rsyslog.service
3. add a log rotation configuration for this log file.
	vim /etc/logrotate.d/rtpproxy
	/var/log/rtpproxy.log {
	  missingok
	  compress
	  delaycompress
	  notifempty
	  copytruncate
	  rotate 5
	  daily
	}
4. add a log facility level to your start row the RTPProxy:
	-d INFO:LOG_LOCAL5
5. Reload systemd configuration and restart your RTPProxy server.
	sudo systemctl daemon-reload
	sudo systemctl restart rtpproxy.service
```
