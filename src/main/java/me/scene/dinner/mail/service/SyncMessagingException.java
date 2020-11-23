package me.scene.dinner.mail.service;

import me.scene.dinner.mail.infra.RuntimeMessagingException;

public class SyncMessagingException extends RuntimeMessagingException {

    public SyncMessagingException(String message) {
        super(message);
    }

}
