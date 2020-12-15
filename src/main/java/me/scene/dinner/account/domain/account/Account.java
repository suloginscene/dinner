package me.scene.dinner.account.domain.account;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.account.domain.tempaccount.TempAccount;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;
import static me.scene.dinner.account.domain.account.AccountRole.USER;

@Entity
@Getter @EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = PROTECTED)
public class Account {

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

    public Account(String username, String email, String encodedPassword) {
        this.username = username;
        this.email = email;
        this.password = encodedPassword;
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
