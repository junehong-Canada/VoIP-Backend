package skychatuc.io.proxyserver.notifications;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class FirebaseCloudMessagingService implements MessagingService
{
    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize()
    {
        try {
            File file = new File(firebaseConfigPath);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(file)))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    //FCM Response Format: projects/{project_id}/messages/{message_id}
    @Override
    public void sendDataMessage(String fcmId, Map<String, String> payload)
            throws InterruptedException, ExecutionException
    {
        AndroidConfig config = AndroidConfig.builder()
                .setDirectBootOk(true)
                .setPriority(AndroidConfig.Priority.HIGH).build();

        Message.Builder message = Message.builder();
        message.setToken(fcmId);
        message.setAndroidConfig(config);
        payload.forEach(message::putData);
        String response = sendAndGetResponse(message.build());
         log.info("Successfully sendDataMessage: " + response);
    }

    @Override
    public void sendPushNotification(PushNotificationRequest request, Map<String, String> payload)
            throws InterruptedException, ExecutionException
    {
        Message.Builder message = getPreconfiguredMessageBuilder(request);
        message.setToken(request.getRegistrationToken());
        message.setNotification(Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getBody())
                .build());
        payload.forEach(message::putData);
        String response = sendAndGetResponse(message.build());
        log.info("Successfully sendPushNotification: Title - " + request.getTitle() + " Message - " + response);
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request)
    {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig);
    }

    private AndroidConfig getAndroidConfig(String topic)
    {
        return AndroidConfig.builder()
                .setTtl(Duration.ofSeconds(30).toMillis()).setCollapseKey(topic)
                .setDirectBootOk(true)
                .setPriority(AndroidConfig.Priority.HIGH)
                .build();
    }

    private ApnsConfig getApnsConfig(String topic)
    {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException
    {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    // Send a message to the devices subscribed to the provided topic.
    @Override
    public void sendMessageToTopic(String topic, String payload) {
        Message message = Message.builder()
                .putData("payload", payload)
                .setTopic(topic)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
             log.info("Successfully sendMessageToTopic: " + response);
        } catch (FirebaseMessagingException e) {
            log.error("Unable to sendMessageToTopic", e);
        }
    }
}
