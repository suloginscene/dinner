package me.scene.paper.service.board.magazine.domain.magazine.model;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.board.magazine.domain.magazine.exception.AuthorizationException;

import java.util.function.Predicate;


@RequiredArgsConstructor
public class Authorization {

    private final Predicate<String> authorize;

    public void check(String username) {
        if (authorize.test(username)) return;
        throw new AuthorizationException(username);
    }

}
