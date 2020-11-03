package me.scene.dinner.domain.account.domain;

import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
public class TempAccount {

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

    public static TempAccount create(String username, String email, String password, PasswordEncoder passwordEncoder) {
        TempAccount tempAccount = new TempAccount();
        tempAccount.username = username;
        tempAccount.email = email;
        tempAccount.password = passwordEncoder.encode(password);
        tempAccount.verificationToken = UUID.randomUUID().toString();
        return tempAccount;
    }

}
