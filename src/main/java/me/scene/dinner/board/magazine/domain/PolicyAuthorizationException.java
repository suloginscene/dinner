package me.scene.dinner.board.magazine.domain;

import org.springframework.security.access.AccessDeniedException;

public class PolicyAuthorizationException extends AccessDeniedException {

    public PolicyAuthorizationException(String username) {
        super(username);
    }

}
