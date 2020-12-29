package me.scene.dinner.integration.service.board.article;

import me.scene.dinner.account.application.listener.NotificationListener;
import me.scene.dinner.board.article.application.command.LikeService;
import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.board.article.domain.article.repository.ArticleRepository;
import me.scene.dinner.board.magazine.domain.magazine.model.Type;
import me.scene.dinner.common.notification.event.NotificationEvent;
import me.scene.dinner.common.notification.message.NotificationMessageFactory;
import me.scene.dinner.integration.utils.ArticleTestHelper;
import me.scene.dinner.integration.utils.MagazineTestHelper;
import me.scene.dinner.integration.utils.TopicTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.BDDMockito.then;


@SpringBootTest
@DisplayName("Article_service(like)")
class LikeServiceTest {

    @Autowired LikeService service;

    @Autowired ArticleRepository repository;

    @Autowired NotificationMessageFactory messageFactory;
    @SpyBean NotificationListener notification;

    @Autowired MagazineTestHelper magazineHelper;
    @Autowired TopicTestHelper topicHelper;
    @Autowired ArticleTestHelper articleHelper;


    Long articleId;

    @BeforeEach
    void create() {
        Long magazineId = magazineHelper.createMagazine("user", "magazine", Type.OPEN);
        Long topicId = topicHelper.createTopic("user", magazineId, "topic");
        articleId = articleHelper.createArticle("user", topicId, "article", true);
    }

    @AfterEach
    void clear() {
        articleHelper.clearArticles();
        topicHelper.clearTopics();
        magazineHelper.clearMagazines();
    }


    @Nested class OnLike {
        @Test
        void publishes() {
            service.like("reader", articleId);

            Article article = repository.find(articleId);
            String message = messageFactory.articleLiked("reader", articleId, article.getTitle());
            NotificationEvent event = new NotificationEvent("user", message);
            then(notification).should().notify(event);
        }
    }

}
