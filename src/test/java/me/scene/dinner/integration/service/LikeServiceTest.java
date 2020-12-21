package me.scene.dinner.integration.service;

import me.scene.dinner.board.magazine.domain.common.Type;
import me.scene.dinner.integration.utils.ArticleTestHelper;
import me.scene.dinner.integration.utils.LikeTestHelper;
import me.scene.dinner.integration.utils.MagazineTestHelper;
import me.scene.dinner.integration.utils.TopicTestHelper;
import me.scene.dinner.like.application.LikeService;
import me.scene.dinner.like.domain.LikedEvent;
import me.scene.dinner.notification.application.NotificationListener;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.BDDMockito.then;


@SpringBootTest
@DisplayName("Like_service")
class LikeServiceTest {

    @Autowired LikeService service;

    @MockBean NotificationListener notification;

    @Autowired LikeTestHelper helper;
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
        helper.clearLikes();
        articleHelper.clearArticles();
        topicHelper.clearTopics();
        magazineHelper.clearMagazines();
    }


    @Nested class OnLike {
        @Test
        void publishes() {
            service.like("reader", articleId);

            LikedEvent event = new LikedEvent("reader", articleId, "user", "article");
            then(notification).should().onUserLikeArticle(event);
        }
    }

}
