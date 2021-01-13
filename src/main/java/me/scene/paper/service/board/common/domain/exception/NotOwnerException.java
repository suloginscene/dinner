package me.scene.paper.service.board.common.domain.exception;

import org.springframework.security.access.AccessDeniedException;


public class NotOwnerException extends AccessDeniedException {

    public NotOwnerException(String username) {
        super(username);
    }

}
