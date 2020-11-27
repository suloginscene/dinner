package me.scene.dinner.account.ui;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class ProfileForm {

    @NotNull
    private String username;

    @NotNull
    private String email;

    @Length(max = 200, message = "소개는 200자 이하여야 합니다.")
    private String introduction;

}
