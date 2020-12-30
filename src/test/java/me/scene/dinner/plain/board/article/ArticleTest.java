package me.scene.dinner.plain.board.article;

import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.board.article.domain.article.model.Reply;
import me.scene.dinner.board.common.domain.exception.NotOwnerException;
import me.scene.dinner.board.magazine.domain.magazine.model.Magazine;
import me.scene.dinner.board.magazine.domain.magazine.model.Type;
import me.scene.dinner.board.topic.domain.model.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Article")
class ArticleTest {

    Article article;

    @BeforeEach
    void create() {
        Magazine magazine = Magazine.create(Type.OPEN, "owner", "magazine", "short", "long");
        Topic topic = new Topic(magazine, "owner", "topic", "short", "long");
        article = new Article(topic, "user", "article", "content", true);
    }


    @Nested class OnCreate {
        @Test
        void has_properties() {
            assertThat(article.getPoint()).isNotNull();
            assertThat(article.getReplies()).isNotNull();
        }
    }

    @Nested class OnUpdate {
        @Test
        void updates() {
            article.update("user", "updated", "content", true);
            assertThat(article.getTitle()).isEqualTo("updated");
        }

        @Nested class WithNotOwner {
            @Test
            void throws_exception() {
                assertThrows(NotOwnerException.class,
                        () -> article.update("notOwner", "updated", "content", true));
            }
        }
    }

    @Nested class OnDelete {
        @Test
        void deletes() {
            article.beforeDelete("user");
        }

        @Nested class OnHasChild {
            @Test
            void does_not_throw() {
                article.getReplies().add(new Reply("user", "reply"));
                assertDoesNotThrow(
                        () -> article.beforeDelete("user")
                );
            }
        }

        @Nested class WithNotOwner {
            @Test
            void throws_exception() {
                assertThrows(NotOwnerException.class,
                        () -> article.beforeDelete("notOwner"));
            }
        }
    }

    @Nested class OnRate {
        @Test
        void propagates() {
            article.rate(100);

            Topic topic = article.getTopic();
            assertThat(topic.getPoint().get()).isEqualTo(100);

            Magazine magazine = article.getTopic().getMagazine();
            assertThat(magazine.getPoint().get()).isEqualTo(100);
        }
    }

}
