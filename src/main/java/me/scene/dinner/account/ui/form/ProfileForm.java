package me.scene.dinner.account.ui.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
@AllArgsConstructor
public class ProfileForm {

    private final String username;
    private final String email;

    @Length(max = 200, message = "소개는 200자 이하여야 합니다.")
    private String greeting;

}
