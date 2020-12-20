package me.scene.dinner.account.application.query.dto;

import lombok.Data;
import me.scene.dinner.account.domain.account.Account;


@Data
public class AccountInfo {

    private final String username;
    private final String email;

    public AccountInfo(Account account) {
        username = account.getUsername();
        email = account.getEmail();
    }

}
