package me.scene.paper.service.board.article.application.command.request;

import lombok.Data;

import java.util.Set;


@Data
public class ArticleCreateRequest {

    private final String username;

    private final Long topicId;

    private final String title;
    private final String content;
    private final boolean publicized;

    private final Set<String> tagNames;

}
