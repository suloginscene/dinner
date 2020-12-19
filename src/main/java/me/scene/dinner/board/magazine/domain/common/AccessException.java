package me.scene.dinner.board.magazine.domain.common;

import org.springframework.security.access.AccessDeniedException;

public class AccessException extends AccessDeniedException {

    protected AccessException(String username, String magazineTitle, Magazine.Type magazineType) {
        super(String.format("%s access %s(%s)", username, magazineTitle, magazineType));
    }

}
