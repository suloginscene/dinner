package me.scene.dinner.board.article.application;

import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.topic.application.TopicService;
import me.scene.dinner.board.topic.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final AccountService accountService;
    private final TopicService topicService;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, AccountService accountService, TopicService topicService) {
        this.articleRepository = articleRepository;
        this.accountService = accountService;
        this.topicService = topicService;
    }

    @Transactional
    public Long save(Long topicId, Long writerId, String title, String content) {
        Topic topic = topicService.find(topicId);
        Article article = Article.create(topic, writerId, title, content);
        article = articleRepository.save(article);
        return article.getId();
    }

    public ArticleDto extractDto(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException(articleId));

        Topic topic = article.getTopic();
        String writer = accountService.find(article.getWriterId()).getUsername();
        return ArticleDto.create(articleId, topic.getId(), topic.getTitle(), writer,
                article.getTitle(), article.getContent(), article.getLocalDateTime());
    }

}
