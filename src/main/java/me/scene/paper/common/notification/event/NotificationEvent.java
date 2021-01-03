package me.scene.paper.common.notification.event;

import lombok.Data;


@Data
public class NotificationEvent {

    private final String receiver;
    private final String message;

}
