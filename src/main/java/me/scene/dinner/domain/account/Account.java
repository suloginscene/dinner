package me.scene.dinner.domain.account;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @EqualsAndHashCode(of = "id")
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private final Set<AccountRole> roles = new HashSet<>();

    protected Account() {
    }

    public Account(SignupForm signupForm) {
        this.username = signupForm.getUsername();
        this.email = signupForm.getEmail();
        this.password = signupForm.getPassword();
        roles.add(AccountRole.USER);
    }

}
