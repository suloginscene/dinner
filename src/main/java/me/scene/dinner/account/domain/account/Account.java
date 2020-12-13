package me.scene.dinner.account.domain.account;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.EnumType.STRING;
import static me.scene.dinner.account.domain.account.AccountRole.USER;

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

    @Enumerated(STRING)
    private final AccountRole role = USER;

    @Embedded
    private Profile profile;

    protected Account() {
    }

    public static Account create(TempAccount tempAccount) {
        Account account = new Account();
        account.username = tempAccount.getUsername();
        account.email = tempAccount.getEmail();
        account.password = tempAccount.getPassword();
        return account;
    }

    public void registerTempPasswordIssuedEvent(String tempRawPassword) {
        registerEvent(new TempPasswordIssuedEvent(email, tempRawPassword));
    }

    public void changePassword(String encodedPassword) {
        password = encodedPassword;
    }

    public void update(String introduction) {
        this.profile = new Profile(introduction);
    }

    public String shortIntroduction() {
        return (profile != null) ? profile.getShortIntroduction() : "";
    }

}
