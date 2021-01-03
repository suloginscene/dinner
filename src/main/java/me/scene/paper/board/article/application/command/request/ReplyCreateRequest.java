package me.scene.paper.board.article.application.command.request;

import lombok.Data;


@Data
public class ReplyCreateRequest {

    private final String username;
    private final Long articleId;
    private final String content;

}
