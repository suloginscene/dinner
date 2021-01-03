package me.scene.paper.account.application.query.dto;

import me.scene.paper.account.domain.account.model.Account;
import me.scene.paper.account.domain.account.model.Profile;

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
