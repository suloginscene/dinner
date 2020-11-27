package me.scene.dinner.test.factory;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.reply.application.ReplyService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplyFactory {

    private final ReplyService replyService;

    public Long create(Long articleId, String writer, String content) {
        return replyService.save(articleId, writer, content);
    }

}
