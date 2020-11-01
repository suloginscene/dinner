package me.scene.dinner.domain.account.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
        username = signupForm.getUsername();
        email = signupForm.getEmail();
        password = signupForm.getPassword();
        roles.add(AccountRole.USER);
    }

    public String changePassword(PasswordEncoder passwordEncoder) {
        String newPassword = UUID.randomUUID().toString();
        password = passwordEncoder.encode(newPassword);
        return newPassword;
    }

    public void changePassword(String rawPassword, PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(rawPassword);
    }

}
