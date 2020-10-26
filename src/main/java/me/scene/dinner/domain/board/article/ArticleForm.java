package me.scene.dinner.domain.board.article;

import lombok.Getter;
import lombok.Setter;

// TODO validation
@Getter @Setter
public class ArticleForm {
    private String title;
    private String content;
}
