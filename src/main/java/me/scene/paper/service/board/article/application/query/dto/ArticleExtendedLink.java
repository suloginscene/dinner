package me.scene.paper.service.board.article.application.query.dto;

import lombok.Getter;
import me.scene.paper.service.board.article.domain.article.model.Article;
import me.scene.paper.service.board.common.dto.Link;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Getter
public class ArticleExtendedLink extends Link {

    private final String owner;

    private final int read;
    private final int like;
    private final int reply;

    private final LocalDateTime createdAt;

    private final List<String> tags;


    public ArticleExtendedLink(Article article) {
        super(article);

        this.owner = article.getOwnerName();

        this.read = article.getRead();
        this.like = article.getLikes().size();
        this.reply = article.getReplies().size();

        this.createdAt = article.getCreatedAt();
        this.tags = article.getArticleTags()
                .stream().map(articleTag -> articleTag.getTag().getName())
                .collect(toList());
    }

}
