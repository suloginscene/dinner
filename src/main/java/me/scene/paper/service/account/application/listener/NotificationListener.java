package me.scene.paper.service.account.application.listener;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.account.application.command.NotificationService;
import me.scene.paper.common.communication.notification.event.NotificationEvent;
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
