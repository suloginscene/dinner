package me.scene.dinner.domain.account.ui;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter @Setter
public class AccountForm {

    @Length(min = 2, max = 16, message = "이름은 2자 이상, 16자 이하여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\-_.]{2,16}$", message = "이름은 한글, 영어, 숫자와 -_.으로 이루어져야 합니다.")
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
