package me.scene.dinner.board.domain.magazine;

import org.springframework.security.access.AccessDeniedException;

public class PolicyAuthorizationException extends AccessDeniedException {

    public PolicyAuthorizationException(String username) {
        super(username);
    }

}
