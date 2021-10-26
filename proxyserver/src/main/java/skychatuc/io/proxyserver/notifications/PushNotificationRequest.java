package skychatuc.io.proxyserver.notifications;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PushNotificationRequest
{
    private String topic;
    private String title;
    private String body;
    private String registrationToken;
}
