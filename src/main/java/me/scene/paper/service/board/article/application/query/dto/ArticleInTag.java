package me.scene.paper.service.board.article.application.query.dto;

import lombok.Getter;
import me.scene.paper.service.board.article.domain.article.model.Article;
import me.scene.paper.service.board.common.dto.Link;

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

        this.owner = article.getOwnerName();

        this.read = article.getRead();
        this.like = article.getLikes().size();
        this.reply = article.getReplies().size();

        this.createdAt = article.getCreatedAt();
    }

}
