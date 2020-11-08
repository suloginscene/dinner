package me.scene.dinner.account.ui;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class ProfileForm {

    @Length(max = 50, message = "짧은 소개는 50자 이하여야 합니다.")
    private String shortIntroduction;

}
