package me.scene.dinner.board.magazine.domain.common;

import org.springframework.security.access.AccessDeniedException;


public class AuthorizationException extends AccessDeniedException {

    protected AuthorizationException(String username) {
        super(String.format("Not authorized: %s", username));
    }

}
