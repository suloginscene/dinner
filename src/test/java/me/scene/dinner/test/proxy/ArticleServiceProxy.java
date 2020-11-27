package me.scene.dinner.test.proxy;

import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.topic.application.TopicService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
public class ArticleServiceProxy extends ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleServiceProxy(ArticleRepository articleRepository, TopicService topicService) {
        super(articleRepository, topicService);
        this.articleRepository = articleRepository;
    }

    public Article load(String title) {
        Article article = articleRepository.findByTitle(title).orElseThrow();
        log.debug("load: " + article.getContent());
        log.debug("load: " + article.getTopic());
        return article;
    }

}
