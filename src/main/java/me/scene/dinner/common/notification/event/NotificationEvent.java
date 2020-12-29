package me.scene.dinner.common.notification.event;

import lombok.Data;


@Data
public class NotificationEvent {

    private final String receiver;
    private final String message;

}
