# Push Notification with Kamailio

https://denys-pozniak.medium.com/apple-push-notification-with-kamailio-eeca2f8e08d

1. Caller sends SIP INVITE to Kamailio SIP Proxy
2. Kamailio detects device type by specific X-header and freezes incoming SIP transaction
3. Kamailio sends “push” via http request to PHP script
4. Once Kamailio detects new REGISTER from device, it resumes transaction
5. Kamailio sends SIP INVITE to the subscriber


Kamailio script building blocks:
```
#Detecting device type via custom SIP header X-phone.
if ( (is_method("INVITE")) && (!has_totag()) && ($(hdr(X-phone) =~ "iphone") ) {
  send_reply("100", "Suspending");
  route(SUSPEND);
}
#Suspending transaction and store index and label in vtp for the future needs.
route[SUSPEND] {
  if ( !t_suspend() ) {
    xlog("L_ERROR","[SUSPEND]  failed suspending trasaction [$T(id_index):$T(id_label)]\n");
    send_reply("501", "Suspending error");
    exit;
  } else {
    xlog("L_INFO","[SUSPEND]  suspended transaction [$T(id_index):$T(id_label)] $fU=> $rU\n");
    $sht(vtp=>id_index::$rU) = $T(id_index);
    $sht(vtp=>id_label::$rU) = $T(id_label);
    xlog("L_INFO","[SUSPEND] htable key value [$sht(vtp=>id_index::$rU)   --   $sht(vtp=>id_label::$rU)]\n");
    route(SENDPUSH);
    exit;
  }
```
In my case Kamailio runs the intermediate PHP script (push.php) with needed parameters for sending request to APN. You might use app_lua module to push directly from Kamailio.
```
#Below is a pushing service. It calls push.php script with parameters. Htable $sht(tokens=>$rU) keeps needed token. And after PHP script connects to APN.
route[SENDPUSH] {
  ...
  http_client_query("http://url/push.php", "user=$rU\r\npn-tok=$sht(tokens=>$rU)\r\n","Content-Type: text/plain", "$var(result)");
  ...
  sl_send_reply("100", "Pushing");
}

Here we are detecting incoming REGISTER message from pushed device:
#Unfreezing by new incoming REGISTER message.
if ( (is_method("REGISTER")) && (($hdr(Expires) != "0") || ($hdr(Contact) !~ "expires=0")) && ($sht(vtp=>id_index::$tU) != $null) ) {
  xlog("L_INFO", "New $rm ru=$ru tu=$tu \n");
  route(JOIN);
}

Resuming SIP transaction using stored label and index:
#Resuming transaction.
route[JOIN] {
  xlog("L_INFO","[JOIN] htable key value [$sht(vtp=>id_index::$tU)   --   $sht(vtp=>id_label::$tU)]\n");
  t_continue("$sht(vtp=>id_index::$tU)", "$sht(vtp=>id_label::$tU)", "RESUME");
}
#Lookup into location database and relaying.
route[RESUME] {
  lookup("location");
  xlog("L_INFO","[RESUME] rm=$rm ru=$ru du=$du \n");
  t_relay();
  exit;
}
```
Sngrep call-flow example: <br>
![Sngrep call-flow example](https://github.com/junehong-Canada/VoIP-Backend/blob/main/signalling/call-flow.png)
