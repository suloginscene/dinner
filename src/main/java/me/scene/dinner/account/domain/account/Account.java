package me.scene.dinner.account.domain.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;
import static me.scene.dinner.account.domain.account.Role.USER;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Account extends BaseEntity {

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Enumerated(STRING)
    private final Role role = USER;

    @Embedded
    private Profile profile = new Profile();


    public Account(String username, String email, String encodedPassword) {
        this.username = username;
        this.email = email;
        this.password = encodedPassword;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void changeIntroduction(Profile profile) {
        this.profile = profile;
    }

}
