package me.scene.paper.account.application.query.dto;

import lombok.Data;
import me.scene.paper.account.domain.account.model.Notification;

import java.time.LocalDateTime;


@Data
public class NotificationData {

    private final Long id;
    private final String message;
    private final LocalDateTime createdAt;

    public NotificationData(Notification notification) {
        id = notification.getId();
        message = notification.getMessage();
        createdAt = notification.getCreatedAt();
    }

}
