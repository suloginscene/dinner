package me.scene.dinner.mail.service;

import me.scene.dinner.mail.infra.RuntimeMessagingException;

public class AsyncMessagingException extends RuntimeMessagingException {

    protected AsyncMessagingException(String message) {
        super(message);
    }

}
