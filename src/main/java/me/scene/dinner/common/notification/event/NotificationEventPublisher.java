package me.scene.dinner.common.notification.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class NotificationEventPublisher {

    private final ApplicationEventPublisher publisher;


    public void publish(String receiver, String message) {
        NotificationEvent event = new NotificationEvent(receiver, message);
        publisher.publishEvent(event);
    }

}
