package me.scene.dinner.account.application.command.notification;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.common.event.NotificationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService service;

    @Async
    @EventListener
    public void notify(NotificationEvent event) {
        String receiver = event.getReceiver();
        String message = event.getMessage();

        service.save(receiver, message);
    }

}
