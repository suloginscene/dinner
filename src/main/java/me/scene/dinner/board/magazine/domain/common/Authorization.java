package me.scene.dinner.board.magazine.domain.common;

import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;


@RequiredArgsConstructor
public class Authorization {

    private final Predicate<String> authorize;

    public void check(String username) {
        if (authorize.test(username)) return;
        throw new AuthorizationException(username);
    }

}
