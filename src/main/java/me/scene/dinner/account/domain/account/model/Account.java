package me.scene.dinner.account.domain.account.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scene.dinner.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Account extends BaseEntity {

    @Column(length = 16, unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(STRING) @Column(nullable = false, length = 5)
    private final Role role = Role.USER;

    @Embedded
    private Profile profile = new Profile("");

    @OneToMany(cascade = ALL, orphanRemoval = true) @JoinColumn(name = "account_id")
    private final List<Notification> notifications = new ArrayList<>();


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
