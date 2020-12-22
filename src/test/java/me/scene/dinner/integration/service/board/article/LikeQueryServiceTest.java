package me.scene.dinner.integration.service.board.article;

import me.scene.dinner.account.application.command.notification.NotificationListener;
import me.scene.dinner.board.article.application.command.LikeService;
import me.scene.dinner.board.article.application.query.LikeQueryService;
import me.scene.dinner.board.magazine.domain.magazine.model.Type;
import me.scene.dinner.integration.utils.ArticleTestHelper;
import me.scene.dinner.integration.utils.MagazineTestHelper;
import me.scene.dinner.integration.utils.TopicTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DisplayName("Article_query(like)")
class LikeQueryServiceTest {

    @Autowired LikeQueryService query;

    @Autowired LikeService service;

    @MockBean NotificationListener notification;

    @Autowired ArticleTestHelper helper;
    @Autowired TopicTestHelper topicHelper;
    @Autowired MagazineTestHelper magazineHelper;


    Long id;

    @BeforeEach
    void create() {
        Long magazineId = magazineHelper.createMagazine("magazineOwner", "magazine", Type.OPEN);
        Long topicId = topicHelper.createTopic("topicOwner", magazineId, "topic");
        id = helper.createArticle("user", topicId, "article", true);

        helper.createArticle("user", topicId, "private", false);
    }


    @Nested class OnQueryDoesLike {
        @Test
        void returns_boolean() {
            boolean before = query.doesLike("reader", id);
            assertThat(before).isFalse();

            service.like("reader", id);

            boolean after = query.doesLike("reader", id);
            assertThat(after).isTrue();
        }
    }

}
