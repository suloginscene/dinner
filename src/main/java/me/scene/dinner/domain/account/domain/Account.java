package me.scene.dinner.domain.account.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @ElementCollection(fetch = EAGER)
    @Enumerated(EnumType.STRING)
    private final Set<AccountRole> roles = new HashSet<>();

    protected Account() {
    }

    public static Account create(TempAccount tempAccount) {
        Account account = new Account();
        account.username = tempAccount.getUsername();
        account.email = tempAccount.getEmail();
        account.password = tempAccount.getPassword();
        account.roles.add(AccountRole.USER);
        return account;
    }

    public void changePassword(String encodedPassword) {
        password = encodedPassword;
    }

}
