package me.scene.dinner.board.magazine.domain;

import org.springframework.security.access.AccessDeniedException;

public class PolicyAuthException extends AccessDeniedException {

    public PolicyAuthException(String username) {
        super(username);
    }

}
