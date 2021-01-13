package me.scene.paper.service.account.application.query.dto;

import lombok.Data;
import me.scene.paper.service.account.domain.account.model.Account;


@Data
public class AccountInfo {

    private final String username;
    private final String email;

    public AccountInfo(Account account) {
        username = account.getUsername();
        email = account.getEmail();
    }

}
