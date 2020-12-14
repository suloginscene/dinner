package me.scene.dinner.test.factory;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.application.article.ReplyService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReplyFactory {

    private final ReplyService replyService;

    public void create(Long articleId, String writer, String content) {
        replyService.save(writer, articleId, content);
    }

}
