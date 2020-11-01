package me.scene.dinner.domain.account.domain;

import lombok.Getter;

@Getter
public class Profile {

    private final String username;

    public Profile(Account account) {
        this.username = account.getUsername();
    }

    public boolean isOwnedBy(Account account) {
        return username.equals(account.getUsername());
    }

}
