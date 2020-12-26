package me.scene.dinner.board.article.application.query.dto;

import lombok.Getter;
import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.board.common.dto.Link;

import java.time.LocalDateTime;


@Getter
public class ArticleInTag extends Link {

    private final String owner;

    private final int read;
    private final int like;
    private final int reply;

    private final LocalDateTime createdAt;


    public ArticleInTag(Article article) {
        super(article);

        this.owner = article.getOwner().name();

        this.read = article.getRead();
        this.like = article.getLikes().size();
        this.reply = article.getReplies().size();

        this.createdAt = article.getCreatedAt();
    }

}
