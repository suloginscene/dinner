package me.scene.paper.board.article.application.command.request;

import lombok.Data;


@Data
public class ArticleUpdateRequest {

    private final String username;

    private final Long id;

    private final String title;
    private final String content;
    private final boolean publicized;

}
