package me.scene.dinner.plain;

import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.magazine.domain.open.OpenMagazine;
import me.scene.dinner.board.topic.domain.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Magazine(open)")
class OpenMagazineTest {

    OpenMagazine magazine;
    Topic topic;

    @BeforeEach
    void create() {
        magazine = new OpenMagazine("owner", "magazine", "short", "long");
        topic = new Topic(magazine, "owner", "title", "short", "long");
    }

    @Nested class OnPublicizeArticle {

        @Test
        void add_writer() {
            new Article(topic, "writer", "title", "content", true);
            assertThat(magazine.writerNames()).contains("writer");
        }

        @Nested class WithMagazineOwner {
            @Test
            void does_not_add_writer() {
                new Article(topic, "owner", "title", "content", true);
                assertThat(magazine.writerNames()).doesNotContain("owner");
            }
        }
    }

    @Nested class OnDisPublicizeArticle {
        @Test
        void remove_writer() {
            Article article = new Article(topic, "writer", "title", "content", true);
            article.beforeDelete("writer");
            assertThat(magazine.writerNames()).doesNotContain("writer");
        }

        @Nested class OnRemain {
            @Test
            void does_not_remove_writer() {
                new Article(topic, "writer", "old", "content", true);
                Article article = new Article(topic, "writer", "title", "content", true);
                article.beforeDelete("writer");
                assertThat(magazine.writerNames()).contains("writer");
            }
        }
    }

}
