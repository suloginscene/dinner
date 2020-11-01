package me.scene.dinner.domain.account.domain;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

@Getter
public class UserAccount extends User {

    private final Account account;

    public UserAccount(Account account) {
        super(account.getUsername(), account.getPassword(),
                account.getRoles().stream()
                        .map(AccountRole::toString)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet()));
        this.account = account;
    }

}
