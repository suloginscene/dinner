package me.scene.dinner.domain.account.domain;

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

    @Length(min = 2, max = 16, message = "이름은 2자 이상, 16자 이하여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\-_.]{2,16}$", message = "이름은 한글, 영어, 숫자와 -_.으로 이루어져야 합니다.")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Email(message = "올바른 이메일이 필요합니다.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Length(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @NotNull(message = "가입에는 동의가 필요합니다.")
    @AssertTrue(message = "가입에는 동의가 필요합니다.")
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
