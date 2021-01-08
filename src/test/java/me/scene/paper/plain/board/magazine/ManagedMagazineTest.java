package me.scene.paper.plain.board.magazine;

import me.scene.paper.board.article.domain.article.model.Article;
import me.scene.paper.board.magazine.domain.magazine.exception.AuthorizationException;
import me.scene.paper.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.paper.board.topic.domain.model.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("Magazine(managed)")
class ManagedMagazineTest {

    ManagedMagazine magazine;

    @BeforeEach
    void create() {
        magazine = new ManagedMagazine("owner", "magazine", "short", "long");
    }


    @Nested class OnManageMember {
        @Test
        void adds_member() {
            magazine.addMember("member");
            assertThat(magazine.memberNames()).contains("member");
        }

        @Test
        void remove_member() {
            magazine.addMember("member");

            magazine.removeMember("member");
            assertThat(magazine.memberNames()).doesNotContain("member");
        }
    }

    @Nested class OnQuitFromMagazine {
        @Test
        void remove_member() {
            magazine.addMember("member");

            magazine.quit("member");
            assertThat(magazine.memberNames()).doesNotContain("member");
        }
    }

    @Nested class OnCreateChild {
        @Nested class WithNotMember {
            @Test
            void throws_exception() {
                assertThrows(
                        AuthorizationException.class,
                        () -> new Topic(magazine, "notMember", "title", "short", "long")
                );

                magazine.addMember("member");
                Topic topic = new Topic(magazine, "member", "title", "short", "long");
                assertThrows(
                        AuthorizationException.class,
                        () -> new Article(topic, "notMember", "title", "content", true)
                );
            }
        }
    }

}
