package me.scene.dinner.account.domain.tempaccount;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter @EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
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

    public TempAccount(String username, String email, String encodedPassword) {
        this.username = username;
        this.email = email;
        this.password = encodedPassword;
        this.verificationToken = UUID.randomUUID().toString();
    }

}
