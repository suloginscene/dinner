package me.scene.dinner.board.reply.application;

import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.reply.domain.Reply;
import me.scene.dinner.board.reply.domain.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
