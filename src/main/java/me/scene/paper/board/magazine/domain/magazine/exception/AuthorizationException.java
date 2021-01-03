package me.scene.paper.board.magazine.domain.magazine.exception;

import org.springframework.security.access.AccessDeniedException;


public class AuthorizationException extends AccessDeniedException {

    public AuthorizationException(String username) {
        super(String.format("Not authorized: %s", username));
    }

}
