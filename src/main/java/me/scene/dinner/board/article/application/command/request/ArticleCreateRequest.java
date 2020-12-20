package me.scene.dinner.board.article.application.command.request;

import lombok.Data;


@Data
public class ArticleCreateRequest {

    private final String username;

    private final Long topicId;

    private final String title;
    private final String content;
    private final boolean publicized;

}
