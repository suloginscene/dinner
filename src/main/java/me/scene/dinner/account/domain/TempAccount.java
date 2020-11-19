package me.scene.dinner.account.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter @EqualsAndHashCode(of = "id", callSuper = false)
public class TempAccount extends AbstractAggregateRoot<TempAccount> {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String verificationToken;

    protected TempAccount() {
    }

    public static TempAccount create(String username, String email, String encodedPassword) {
        TempAccount tempAccount = new TempAccount();
        tempAccount.username = username;
        tempAccount.email = email;
        tempAccount.password = encodedPassword;
        tempAccount.verificationToken = UUID.randomUUID().toString();
        tempAccount.registerEvent(new TempAccountCreatedEvent(tempAccount, email, tempAccount.verificationToken));
        return tempAccount;
    }

    public void verify(String token) {
        if (token.equals(verificationToken)) return;
        throw new VerificationException(token);
    }

}
