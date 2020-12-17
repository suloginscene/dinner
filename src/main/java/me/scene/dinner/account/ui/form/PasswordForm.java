package me.scene.dinner.account.ui.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordForm {

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Length(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

}
