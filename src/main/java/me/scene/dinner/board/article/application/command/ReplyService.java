package me.scene.dinner.board.article.application.command;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.command.request.ReplyCreateRequest;
import me.scene.dinner.board.article.application.command.request.ReplyDeleteRequest;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.article.domain.Reply;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ArticleRepository repository;

    public void save(ReplyCreateRequest request) {
        Long articleId = request.getArticleId();
        String username = request.getUsername();
        String content = request.getContent();

        Reply reply = new Reply(username, content);

        Article article = repository.find(articleId);
        article.add(reply);

        // TODO notification
    }

    public void delete(ReplyDeleteRequest request) {
        Long articleId = request.getArticleId();
        Long replyId = request.getReplyId();
        String username = request.getUsername();

        Article article = repository.find(articleId);
        article.remove(replyId, username);
    }

}
