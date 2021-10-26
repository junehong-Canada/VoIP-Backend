package skychatuc.io.proxyserver.notifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class PushNotificationService
{
    @Autowired
    private FirebaseCloudMessagingService messagingService;

    public void sendDataMessage(PushNotificationRequest request, Map<String, String> payload)
    {
        try {
            messagingService.sendDataMessage(request.getRegistrationToken(), payload);
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }

    public void sendPushNotification(PushNotificationRequest request, Map<String, String> payload)
    {
        try {
            messagingService.sendPushNotification(request, payload);
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
        }
    }

    public void sendMessageToTopic(String topic, String payload)
    {
        messagingService.sendMessageToTopic(topic, payload);
    }
}
