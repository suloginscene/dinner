package me.scene.paper.common.communication.notification.publisher;

import lombok.RequiredArgsConstructor;
import me.scene.paper.common.communication.notification.event.NotificationEvent;
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
