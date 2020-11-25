package me.scene.dinner.account.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.scene.dinner.account.domain.tempaccount.TempAccount;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @EqualsAndHashCode(of = "id", callSuper = false)
public class Account extends AbstractAggregateRoot<Account> {

    @Id @GeneratedValue @JsonIgnore
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING) @JsonIgnore
    private final AccountRole role = AccountRole.USER;

    @Embedded @JsonIgnore
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
        registerEvent(new TempPasswordIssuedEvent(this, email, tempRawPassword));
    }

    public void changePassword(String encodedPassword) {
        password = encodedPassword;
    }

    public void update(Profile profile) {
        this.profile = profile;
    }

}
