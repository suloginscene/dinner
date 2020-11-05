package me.scene.dinner.board.magazine.ui;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter @Setter @ToString
public class MagazineForm {

    @NotBlank(message = "제목을 적어주세요.")
    @Length(max = 20, message = "제목은 최대 30자까지 가능합니다.")
    private String title;

    @NotBlank(message = "짧은 소개를 적어주세요.")
    @Length(max = 30, message = "짧은 소개는 최대 30자까지 가능합니다.")
    private String shortExplanation;

    @NotBlank(message = "자세한 소개를 적어주세요.")
    @Length(max = 250, message = "자세한 소개는 최대 250자까지 가능합니다.")
    private String longExplanation;

    @NotBlank(message = "매거진 성격을 선택해주세요.")
    private String magazinePolicy;

}
