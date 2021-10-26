# Push Notification with Kamailio

https://denys-pozniak.medium.com/apple-push-notification-with-kamailio-eeca2f8e08d

1. Caller sends SIP INVITE to Kamailio SIP Proxy
2. Kamailio detects device type by specific X-header and freezes incoming SIP transaction
3. Kamailio sends “push” via http request to PHP script
4. Once Kamailio detects new REGISTER from device, it resumes transaction
5. Kamailio sends SIP INVITE to the subscriber


Kamailio script building blocks:
```
$ sudo vi /etc/kamailio/kamailio.cfg
    :
#!define WITH_SENDPUSH
    :
#!ifdef WITH_SENDPUSH
loadmodule "htable.so"
loadmodule "http_client.so"
#!endif
    :
#!ifdef WITH_SENDPUSH
/* vtp keeps transaction details. */
modparam("htable", "htable", "vtp=>size=10;autoexpire=120;")
modparam("http_client", "httpcon", "pushserver=>http://localhost:8888");
#!endif
    :
# send the push notification
#!ifdef WITH_SENDPUSH
	route(PUSHASYNC);
#!endif
    :
#!ifdef WITH_SENDPUSH
# Do push in async mode
route[PUSHASYNC] {
	if (!is_method("INVITE"))
		return;
	
	if (registered("location"))
		return;

	route(SENDPUSH);

	if (!t_suspend()) {
		xlog("failed suspending transaction [$T(id_index):$T(id_label)]\n");
		send_reply("501", "Unknown destination");
		exit;
	}
	xdbg("suspended transaction [$T(id_index):$T(id_label)] $fU => $rU\n");
	$sht(vtp=>join::$rU) = "" + $T(id_index) + ":" + $T(id_label);
	xdbg("htable key value [$sht(vtp=>join::$rU)]\n");
	exit;
}

route[SENDPUSH] {
	# POST-Request
	# $var(res) = http_connect("pushserver", "/sipuser/sendpush", "application/json", "{ username : [$T(id_index)] }", "$avp(gurka)");
	xlog("L_INFO", "===> Suspend call from [$fU] to [$tU] and send push notification");
	http_client_query("http://localhost:8888/sipuser/sendpush/$tU", "$var(result)");
	xlog("L_INFO", "===> Result is $var(result)");
	send_reply("110", "Push sent");
} 
#!endif
    :
#!ifdef WITH_SENDPUSH
	route(PUSHJOIN);
#!endif
    :
#!ifdef WITH_SENDPUSH
# Join pending INVITE with the incoming REGISTER
route[PUSHJOIN] {
	if (!is_method("REGISTER"))
		return;
	$var(hjoin) = 0;
#	lock("$tU");
	$var(hjoin) = $sht(vtp=>join::$tU);
	$sht(vtp=>join::$tU) = $null;
#	unlock("$tU);
	if ($var(hjoin)==0)
		return;
	$var(id_index) = $(var(hjoin){s.select,0,:}{s.int});
	$var(id_label) = $(var(hjoin){s.select,1,:}{s.int});
	xdbg("resuming transaction [$var(id_index):$var(id_label)] $tU ($var(hjoin))\n");
	t_continue("$var(id_index)", "$var(id_label)", "LOCATION");
}
#!endif
```
Sngrep call-flow example: <br>
![Sngrep call-flow example](https://github.com/junehong-Canada/VoIP-Backend/blob/main/signalling/call-flow.png)
