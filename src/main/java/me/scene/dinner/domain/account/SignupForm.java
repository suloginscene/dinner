package me.scene.dinner.domain.account;

import lombok.Getter;
import lombok.Setter;
import me.scene.dinner.infra.exception.VerificationException;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.util.UUID;

@Entity
@Getter @Setter
public class SignupForm {

    @Id @GeneratedValue
    Long id;

    @NotBlank @Length(min = 2, max = 16)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\-_.]{2,16}$")
    @Column(unique = true)
    private String username;

    @NotBlank @Email
    @Column(unique = true)
    private String email;

    @NotBlank @Length(min = 8)
    private String password;

    @NotNull @AssertTrue
    private Boolean agreement;

    private String verificationToken;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        password = passwordEncoder.encode(password);
    }

    public void generateVerificationToken() {
        verificationToken = UUID.randomUUID().toString();
    }

    public void validateToken(String token) {
        if (!token.equals(verificationToken)) throw new VerificationException(email);
    }
}
