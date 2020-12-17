package me.scene.dinner.account.domain.tempaccount;

import me.scene.dinner.common.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class TempAccount extends BaseEntity {

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String verificationToken;


    public TempAccount(String username, String email, String encodedPassword) {
        this.username = username;
        this.email = email;
        this.password = encodedPassword;
        this.verificationToken = UUID.randomUUID().toString();
    }

    public TempAccountCreatedEvent createdEvent() {
        return new TempAccountCreatedEvent(email, verificationToken);
    }

    public void verify(String token) {
        if (token.equals(verificationToken)) return;
        throw new VerificationException(token);
    }

}
