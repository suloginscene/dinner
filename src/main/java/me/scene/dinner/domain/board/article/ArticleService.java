package me.scene.dinner.domain.board.article;

import me.scene.dinner.domain.account.Account;
import me.scene.dinner.domain.board.topic.Topic;
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
    public String createArticle(Account current, String topic, ArticleForm articleForm) {
        // TODO
//        Topic topic = topicRepository.findByTitle(articleForm.getTopic()).orElseThrow();
        Topic topic1 = new Topic();
        topicRepository.save(topic1);
        Article article = new Article(current, topic1, articleForm);
        article = articleRepository.save(article);
        return article.getTitle();
    }

}
