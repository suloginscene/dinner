package me.scene.dinner.common.exception;


import org.springframework.security.access.AccessDeniedException;

public class NotOwnerException extends AccessDeniedException {

    public NotOwnerException(String username) {
        super(username);
    }

}
