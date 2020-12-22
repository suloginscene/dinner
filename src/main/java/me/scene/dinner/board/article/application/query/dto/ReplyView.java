package me.scene.dinner.board.article.application.query.dto;

import lombok.Data;
import me.scene.dinner.board.article.domain.article.model.Reply;

import java.time.LocalDateTime;


@Data
public class ReplyView {

    private final Long id;
    private final String owner;
    private final String content;
    private final LocalDateTime createdAt;


    public ReplyView(Reply reply) {
        id = reply.getId();
        owner = reply.getOwner().name();
        content = reply.getContent();
        createdAt = reply.getCreatedAt();
    }

}
