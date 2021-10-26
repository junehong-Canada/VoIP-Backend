# RTP Proxy with Kamailio

Kamailio script building blocks:
```
$ sudo vi /etc/kamailio/kamailio.cfg
    :
#!define WITH_NAT
    :
#!ifdef WITH_NAT
loadmodule "nathelper.so"
#!ifdef WITH_RTPENGINE
loadmodule "rtpengine.so"
#!else
loadmodule "rtpproxy.so"
#!endif
#!endif
    :
#!ifdef WITH_NAT
#!ifdef WITH_RTPENGINE
# ----- rtpengine params -----
modparam("rtpengine", "rtpengine_sock", "udp:127.0.0.1:2223")
#!else
# ----- rtpproxy params -----
# modparam("rtpproxy", "rtpproxy_sock", "unix:/var/run/rtpproxy/rtpproxy.sock")
modparam("rtpproxy", "rtpproxy_sock", "udp:10.89.89.61:7722")
# modparam("rtpproxy", "rtpproxy_sock", "udp:64.141.83.123:7722")
#!endif
    :
```
