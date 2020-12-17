package me.scene.dinner.board.article.command.application;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.article.domain.Reply;
import me.scene.dinner.board.common.BoardNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ArticleRepository articleRepository;

    public void save(String writer, Long articleId, String content) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        Reply reply = new Reply(writer, content);
        article.add(reply);
    }

    public void delete(String current, Long articleId, Long replyId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        Reply reply = article.findReplyById(replyId).orElseThrow(() -> new BoardNotFoundException(replyId));
        reply.getOwner().identify(current);
        article.remove(reply);
    }

}
