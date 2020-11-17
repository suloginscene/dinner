package me.scene.dinner.board.article.domain;

import lombok.Getter;
import me.scene.dinner.board.reply.domain.Reply;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class ArticleDeletedEvent extends ApplicationEvent {

    private final List<Reply> replies;

    public ArticleDeletedEvent(Object source, List<Reply> replies) {
        super(source);
        this.replies = replies;
    }

}
