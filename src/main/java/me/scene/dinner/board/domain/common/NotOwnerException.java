package me.scene.dinner.board.domain.common;

import org.springframework.security.access.AccessDeniedException;

public class NotOwnerException extends AccessDeniedException {

    public NotOwnerException(String username) {
        super(username);
    }

}
