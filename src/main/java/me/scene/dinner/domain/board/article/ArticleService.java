package me.scene.dinner.domain.board.article;

import me.scene.dinner.domain.account.Account;
import me.scene.dinner.domain.board.topic.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TopicRepository topicRepository;


    @Autowired
    public ArticleService(ArticleRepository articleRepository, TopicRepository topicRepository) {
        this.articleRepository = articleRepository;
        this.topicRepository = topicRepository;
    }

    @Transactional
    public Long createArticle(Account current, ArticleForm articleForm) {
        // TODO
//        Topic topic = topicRepository.findByTitle(articleForm.getTopic()).orElseThrow();
        Article article = new Article(current, articleForm);
        article = articleRepository.save(article);
        return article.getId();
    }

}
