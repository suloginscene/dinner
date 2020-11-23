package me.scene.dinner.board.common.exception;

import org.springframework.security.access.AccessDeniedException;

public class NotOwnerException extends AccessDeniedException {

    public NotOwnerException(String username) {
        super(username);
    }

}
