package me.scene.paper.service.board.article.application.command.request;

import lombok.Data;


@Data
public class ReplyDeleteRequest {

    private final String username;
    private final Long articleId;
    private final Long replyId;

}
