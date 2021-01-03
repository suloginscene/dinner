package me.scene.paper.account.domain.tempaccount.model;

import me.scene.paper.account.domain.tempaccount.exception.VerificationException;
import me.scene.paper.common.entity.BaseEntity;

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

    @Column(length = 16, unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String verificationToken;


    public TempAccount(String username, String email, String encodedPassword) {
        this.username = username;
        this.email = email;
        this.password = encodedPassword;
        this.verificationToken = UUID.randomUUID().toString();
    }

    public void verify(String token) {
        if (token.equals(verificationToken)) return;
        throw new VerificationException(token);
    }

}
