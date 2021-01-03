package me.scene.paper.account.ui.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
public class SignupForm {

    @Length(min = 2, max = 16, message = "이름은 2자 이상, 16자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z가-힣0-9\\-_]{2,16}$", message = "이름에는 한글, 영어 소문자, 숫자, -와 _를 사용할 수 있습니다.")
    private String username;

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Email(message = "올바른 이메일이 필요합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Length(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @NotNull(message = "가입에는 동의가 필요합니다.")
    @AssertTrue(message = "가입에는 동의가 필요합니다.")
    private Boolean agreement;

}
