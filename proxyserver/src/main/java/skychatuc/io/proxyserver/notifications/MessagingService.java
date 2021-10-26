package skychatuc.io.proxyserver.notifications;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface MessagingService
{
    void sendDataMessage(String registrationToken, Map<String, String> payload) throws InterruptedException, ExecutionException;

    void sendPushNotification(PushNotificationRequest request, Map<String, String> payload) throws InterruptedException, ExecutionException;

    void sendMessageToTopic(String topic, String payload);
}
