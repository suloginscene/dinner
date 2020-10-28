package me.scene.dinner.domain.board.article;

import lombok.Getter;
import lombok.Setter;
import me.scene.dinner.domain.board.topic.Topic;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class ArticleForm {

    @NotBlank @Length(min = 1, max = 30)
    private String title;

    @NotBlank
    private String content;

    private Topic topic;

}
