package me.scene.dinner.account.domain.account;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @EqualsAndHashCode(of = "id", callSuper = false)
public class Account extends AbstractAggregateRoot<Account> {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @ElementCollection(fetch = LAZY) @Enumerated(EnumType.STRING)
    private final Set<AccountRole> roles = new HashSet<>();

    @Embedded
    private Profile profile;

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

    public void registerTempPasswordIssuedEvent(String tempRawPassword) {
        registerEvent(new TempPasswordIssuedEvent(this, email, tempRawPassword));
    }

    public void changePassword(String encodedPassword) {
        password = encodedPassword;
    }

    public void update(Profile profile) {
        this.profile = profile;
    }

}
