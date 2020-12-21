package me.scene.dinner.account.application.query;

import me.scene.dinner.account.domain.account.Account;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;


@Getter
public class UserAccount extends User {

    private final Account account;

    public UserAccount(Account account) {
        super(account.getUsername(), account.getPassword(), Set.of(new SimpleGrantedAuthority(account.getRole().toString())));
        this.account = account;
    }

}