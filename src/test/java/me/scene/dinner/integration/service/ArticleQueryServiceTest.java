package me.scene.dinner.integration.service;

import me.scene.dinner.board.article.application.query.ArticleQueryService;
import me.scene.dinner.board.article.application.query.dto.ArticleExtendedLink;
import me.scene.dinner.board.article.application.query.dto.ArticleView;
import me.scene.dinner.board.magazine.domain.common.Type;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DisplayName("Article_query")
class ArticleQueryServiceTest {

    @Autowired ArticleQueryService query;

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

    @AfterEach
    void clear() {
        helper.clearArticles();
        topicHelper.clearTopics();
        magazineHelper.clearMagazines();
    }


    @Nested class OnFindView {
        @Test
        void load_topic() {
            ArticleView article = query.find(id);
            assertThat(article.getTopic()).isNotNull();
        }
    }

    @Nested class OnFindPublicByUser {
        @Test
        void returns_list() {
            List<ArticleExtendedLink> articles = query.findPublicByWriter("user");
            assertThat(articles.size()).isEqualTo(1);
        }
    }

    @Nested class OnFindPrivateByUser {
        @Test
        void returns_list() {
            List<ArticleExtendedLink> articles = query.findPrivateByWriter("user");
            assertThat(articles.size()).isEqualTo(1);
        }
    }

}
