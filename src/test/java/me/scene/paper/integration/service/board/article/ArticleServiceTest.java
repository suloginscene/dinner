package me.scene.paper.integration.service.board.article;

import me.scene.paper.account.application.listener.NotificationListener;
import me.scene.paper.board.article.application.command.ArticleService;
import me.scene.paper.board.article.application.command.request.ArticleCreateRequest;
import me.scene.paper.board.article.application.query.ArticleQueryService;
import me.scene.paper.board.article.application.query.TagQueryService;
import me.scene.paper.board.article.application.query.dto.ArticleView;
import me.scene.paper.board.article.application.query.dto.TagView;
import me.scene.paper.board.article.domain.article.model.Article;
import me.scene.paper.board.article.domain.article.repository.ArticleRepository;
import me.scene.paper.board.magazine.domain.magazine.model.Type;
import me.scene.paper.common.notification.event.NotificationEvent;
import me.scene.paper.common.notification.message.NotificationMessageFactory;
import me.scene.paper.integration.utils.ArticleTestHelper;
import me.scene.paper.integration.utils.MagazineTestHelper;
import me.scene.paper.integration.utils.TagTestHelper;
import me.scene.paper.integration.utils.TopicTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.then;


@SpringBootTest
@DisplayName("Article_service")
class ArticleServiceTest {

    @Autowired ArticleService service;

    @Autowired ArticleQueryService query;
    @Autowired TagQueryService tagQuery;

    @Autowired ArticleRepository repository;

    @Autowired NotificationMessageFactory messageFactory;
    @SpyBean NotificationListener notification;

    @Autowired ArticleTestHelper helper;
    @Autowired TopicTestHelper topicHelper;
    @Autowired MagazineTestHelper magazineHelper;
    @Autowired TagTestHelper tagHelper;


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

        tagHelper.clearTags();
    }


    @Nested class OnCreate {
        @Test
        void tags() {
            Set<String> tagNames = Set.of("tag", "names");
            tagNames.forEach(tagHelper::createTag);

            ArticleCreateRequest request = new ArticleCreateRequest("user", topicId, "article", "content", true, tagNames);
            Long id = service.save(request);

            ArticleView article = query.view(id);
            assertThat(article.getTags().size()).isEqualTo(2);

            TagView tag = tagQuery.view("tag");
            assertThat(tag.getArticles().size()).isEqualTo(1);
        }

        @Nested class OnNonExistentTag {
            @Test
            void throws_exception() {
                Set<String> tagNames = Set.of("tag", "names");
                ArticleCreateRequest request = new ArticleCreateRequest("user", topicId, "article", "content", true, tagNames);
                assertThrows(NoSuchElementException.class,
                        () -> service.save(request));
            }
        }
    }

    @Nested class OnDelete {
        @Test
        void orphan_removes() {
            Set<String> tagNames = Set.of("tag", "names");
            tagNames.forEach(tagHelper::createTag);
            ArticleCreateRequest request = new ArticleCreateRequest("user", topicId, "article", "content", true, tagNames);
            Long id = service.save(request);

            service.delete(id, "user");

            assertThrows(NoSuchElementException.class, () -> tagQuery.view("tag"));
        }
    }

    @Nested class OnLike {
        @Test
        void publishes() {
            Long articleId = helper.createArticle("user", topicId, "article", true);
            service.like("reader", articleId);

            Article article = repository.find(articleId);
            String message = messageFactory.articleLiked("reader", articleId, article.getTitle());
            NotificationEvent event = new NotificationEvent("user", message);
            then(notification).should().notify(event);
        }
    }

}
