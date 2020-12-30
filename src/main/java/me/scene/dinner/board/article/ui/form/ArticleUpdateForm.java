package me.scene.dinner.board.article.ui.form;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


@Data
@Builder
public class ArticleUpdateForm {

    private final Long topicId;

    @NotBlank(message = "제목을 적어주세요.")
    @Length(max = 20, message = "제목은 최대 20자까지 가능합니다.")
    private String title;

    @NotBlank(message = "본문을 적어주세요.")
    private String content;

    @NotBlank(message = "공개 여부를 선택해주세요.")
    private String status;

    private String jsonTags;

}
