package me.scene.dinner.account.application.query.dto;

import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.account.domain.account.Profile;

import lombok.Data;


@Data
public class AccountView {

    private final String username;
    private final String email;
    private final Profile profile;

    public AccountView(Account account) {
        username = account.getUsername();
        email = account.getEmail();
        profile = account.getProfile();
    }

}
