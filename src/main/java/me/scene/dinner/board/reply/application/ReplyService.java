package me.scene.dinner.board.reply.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.reply.domain.Reply;
import me.scene.dinner.board.reply.domain.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ArticleService articleService;

    @Transactional
    public void save(Long articleId, String writer, String content) {
        Reply reply = Reply.create(articleService.find(articleId), writer, content);
        replyRepository.save(reply);
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
