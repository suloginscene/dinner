package me.scene.dinner.board.magazine.domain;

public class AuthorizationException extends IllegalArgumentException {
    public AuthorizationException(String username) {
        super(username);
    }
}
