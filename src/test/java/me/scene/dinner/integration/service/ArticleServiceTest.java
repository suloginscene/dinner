package me.scene.dinner.integration.service;

import me.scene.dinner.board.article.application.command.ArticleService;
import me.scene.dinner.board.article.application.command.ArticleTaggedEvent;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.article.domain.ArticleRepository;
import me.scene.dinner.board.magazine.domain.common.Type;
import me.scene.dinner.tag.application.ArticleTaggedEventListener;
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
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;

import static org.mockito.BDDMockito.then;


@SpringBootTest
@DisplayName("Article_service")
class ArticleServiceTest {

    @Autowired ArticleService service;

    @Autowired ArticleRepository repository;

    @MockBean ArticleTaggedEventListener tag;

    @Autowired ArticleTestHelper helper;
    @Autowired TopicTestHelper topicHelper;
    @Autowired MagazineTestHelper magazineHelper;


    Long topicId;

    @BeforeEach
    void create() {
        Long magazineId = magazineHelper.createMagazine("magazineOwner", "magazine", Type.OPEN);
        topicId = topicHelper.createTopic("topicOwner", magazineId, "topic");
    }

    @AfterEach
    void clear() {
        helper.clearArticles();
        topicHelper.clearTopics();
        magazineHelper.clearMagazines();
    }


    @Nested class OnTagged {
        @Test
        void publishes() {
            Long id = helper.createArticle("articleWriter", topicId, "article", true);

            Set<String> tagNames = Set.of("tag", "java");
            service.publishTaggedEvent(id, tagNames);

            Article article = repository.find(id);
            ArticleTaggedEvent event = new ArticleTaggedEvent(article, tagNames);
            then(tag).should().renew(event);
        }
    }

}
