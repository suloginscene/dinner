package me.scene.dinner.board.reply.application;

import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.reply.domain.Reply;
import me.scene.dinner.board.reply.domain.ReplyRepository;
import me.scene.dinner.common.exception.BoardNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service @Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final ArticleService articleService;

    @Autowired
    public ReplyService(ReplyRepository replyRepository, ArticleService articleService) {
        this.replyRepository = replyRepository;
        this.articleService = articleService;
    }

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
        reply.confirmWriter(current);
        replyRepository.delete(reply);
        return reply.getArticle().getId();
    }

}