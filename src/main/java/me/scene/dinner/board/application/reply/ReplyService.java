package me.scene.dinner.board.application.reply;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.application.article.ArticleService;
import me.scene.dinner.board.domain.reply.Reply;
import me.scene.dinner.board.domain.reply.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ArticleService articleService;

    @Transactional
    public Long save(Long articleId, String writer, String content) {
        Reply reply = Reply.create(articleService.find(articleId), writer, content);
        return replyRepository.save(reply).getId();
    }

    private Reply find(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> new ReplyNotFoundException(id));
    }

    @Transactional
    public Long delete(Long id, String current) {
        Reply reply = find(id);
        reply.beforeDelete(current);
        replyRepository.delete(reply);
        return reply.getArticle().getId();
    }

}
