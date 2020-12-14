package me.scene.dinner.board.application.article;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.domain.article.Article;
import me.scene.dinner.board.domain.article.ArticleRepository;
import me.scene.dinner.board.domain.article.Reply;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ArticleRepository articleRepository;

    public void save(String writer, Long articleId, String content) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        Reply reply = Reply.create(writer, content);
        article.add(reply);
    }

    public void delete(String current, Long articleId, Long replyId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        Reply reply = article.findReplyById(replyId).orElseThrow(() -> new ReplyNotFoundException(replyId));
        reply.confirmWriter(current);
        article.remove(reply);
    }

}
