package me.scene.dinner.account.application;

import lombok.Getter;
import me.scene.dinner.account.domain.Account;

@Getter
public class AccountDto {

    private final String username;

    public AccountDto(Account account) {
        this.username = account.getUsername();
    }

    public boolean isOwnedBy(Account account) {
        return username.equals(account.getUsername());
    }

}
