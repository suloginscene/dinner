package me.scene.dinner.plain.board.topic;

import me.scene.dinner.board.common.domain.exception.NotDeletableException;
import me.scene.dinner.board.common.domain.exception.NotOwnerException;
import me.scene.dinner.board.magazine.domain.magazine.model.Magazine;
import me.scene.dinner.board.magazine.domain.magazine.model.Type;
import me.scene.dinner.board.topic.domain.model.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;


@DisplayName("Topic")
class TopicTest {

    Topic topic;

    @BeforeEach
    void create() {
        Magazine magazine = Magazine.create(Type.OPEN, "owner", "magazine", "short", "long");
        topic = new Topic(magazine, "owner", "topic", "short", "long");
    }


    @Nested class OnCreate {
        @Test
        void has_properties() {
            assertThat(topic.getPoint()).isNotNull();
            assertThat(topic.getArticles()).isNotNull();
        }
    }

    @Nested class OnUpdate {
        @Test
        void updates() {
            topic.update("owner", "updated", "short", "long");
            assertThat(topic.getTitle()).isEqualTo("updated");
        }

        @Nested class WithNotOwner {
            @Test
            void throws_exception() {
                assertThrows(NotOwnerException.class,
                        () -> topic.update("notOwner", "updated", "short", "long"));
            }
        }
    }

    @Nested class OnDelete {
        @Test
        void deletes() {
            topic.beforeDelete("owner");
        }

        @Nested class OnHasChild {
            @Test
            void throws_exception() {
                topic.getArticles().add();
                assertThrows(NotDeletableException.class,
                        () -> topic.beforeDelete("owner"));
            }
        }

        @Nested class WithNotOwner {
            @Test
            void throws_exception() {
                assertThrows(NotOwnerException.class,
                        () -> topic.beforeDelete("notOwner"));
            }
        }
    }


}