package me.scene.dinner.domain.board.article;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class ArticleForm {

    @NotBlank @Length(max = 30)
    private String title;

    @NotBlank @Pattern(regexp = "^[a-z0-9\\-]{2,20}$")
    private String url;

    @NotBlank
    private String content;

    @NotBlank
    private String topic;

}
