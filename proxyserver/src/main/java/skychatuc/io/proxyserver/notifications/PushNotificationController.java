package skychatuc.io.proxyserver.notifications;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import skychatuc.io.proxyserver.sip.ResourceNotFoundException;
import skychatuc.io.proxyserver.sip.Subscriber;
import skychatuc.io.proxyserver.sip.SubscriberRepository;

import javax.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;

@Getter
@Setter
@Slf4j
@RestController
public class PushNotificationController {
    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private PushNotificationService pushNotificationService;

    @PostMapping("/sipuser/sendpush")
    public ResponseEntity<String> sendPush_(@Valid @RequestBody Subscriber subscriberDetails)
            throws ResourceNotFoundException {
        log.info("/sipuser/sendpush " + subscriberDetails.getUsername());
        Subscriber subscriber = subscriberRepository.findByUsername(subscriberDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found for this username :: " + subscriberDetails.getUsername()));
        String fcmId = subscriber.getFcmId();
        log.info("send PushNotification to " + fcmId);
        PushNotificationRequest request = PushNotificationRequest.builder()
                .title("You have incoming call!")
                .registrationToken(fcmId)
                .build();
        // Send a data message via firebase to the client
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("body", fcmId);
        pushNotificationService.sendDataMessage(request, notificationData);
        return ResponseEntity.ok().body(fcmId);
    }

    @GetMapping("/sipuser/sendpush/{username}")
    public ResponseEntity<String> sendPush(@PathVariable(value = "username") String username)
            throws ResourceNotFoundException {
        log.info("/sipuser/sendpush/" + username);
        Subscriber subscriber = subscriberRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found for this username :: " + username));
        String fcmId = subscriber.getFcmId();
        log.info("send PushNotification to " + fcmId);
        PushNotificationRequest request = PushNotificationRequest.builder()
                .title("You have incoming call!")
                .registrationToken(fcmId)
                .build();
        // Send a data message via firebase to the client
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("body", fcmId);
        pushNotificationService.sendDataMessage(request, notificationData);
        return ResponseEntity.ok().body(fcmId);
    }

    @GetMapping("/sipuser/sendpush1/{fcmId}")
    public ResponseEntity<String> sendPush1(@PathVariable(value = "fcmId") String fcmId)
            throws ResourceNotFoundException {
        log.info("/sipuser/sendpush1/" + fcmId);
       log.info("send PushNotification to " + fcmId);
        PushNotificationRequest request = PushNotificationRequest.builder()
                .title("You have incoming call!")
                .registrationToken(fcmId)
                .build();
        // Send a push notification via firebase to the client
        pushNotificationService.sendPushNotification(request, emptyMap());
        return ResponseEntity.ok().body(fcmId);
    }

    @GetMapping("/sipuser/sendpush2/{fcmId}")
    public ResponseEntity<String> sendPush2(@PathVariable(value = "fcmId") String fcmId)
            throws ResourceNotFoundException {
        log.info("/sipuser/sendpush2/" + fcmId);
        log.info("send PushNotification to " + fcmId);
        PushNotificationRequest request = PushNotificationRequest.builder()
                .title("You have incoming call!")
                .registrationToken(fcmId)
                .build();
        // Send a data message via firebase to the client
        Map<String, String> notificationData = new HashMap<>();
        notificationData.put("body", fcmId);
        pushNotificationService.sendDataMessage(request, notificationData);
        return ResponseEntity.ok().body(fcmId);
    }
}
