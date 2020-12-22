package me.scene.dinner.common.event;

import lombok.Data;


@Data
public abstract class NotificationEvent {

    private final String receiver;
    private final String message;

}
