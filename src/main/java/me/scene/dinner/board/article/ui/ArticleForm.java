package me.scene.dinner.board.article.ui;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ArticleForm {

    @NotNull(message = "토픽 ID가 있어야 합니다.")
    private Long topicId;

    @NotBlank(message = "제목을 적어주세요.")
    @Length(max = 30, message = "제목은 최대 30자까지 가능합니다.")
    private String title;

    @NotBlank(message = "본문을 적어주세요.")
    private String content;

    @NotBlank(message = "공개 여부를 선택해주세요.")
    private String status;

}
