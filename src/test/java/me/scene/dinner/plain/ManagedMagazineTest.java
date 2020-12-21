package me.scene.dinner.plain;

import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.board.magazine.domain.common.AuthorizationException;
import me.scene.dinner.board.magazine.domain.managed.ManagedMagazine;
import me.scene.dinner.board.topic.domain.Topic;
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
            magazine.addMember("owner", "member");
            assertThat(magazine.memberNames()).contains("member");
        }

        @Test
        void remove_member() {
            magazine.addMember("owner", "member");

            magazine.removeMember("owner", "member");
            assertThat(magazine.memberNames()).doesNotContain("member");
        }
    }

    @Nested class OnQuitFromMagazine {
        @Test
        void remove_member() {
            magazine.addMember("owner", "member");

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

                magazine.addMember("owner", "member");
                Topic topic = new Topic(magazine, "member", "title", "short", "long");
                assertThrows(
                        AuthorizationException.class,
                        () -> new Article(topic, "notMember", "title", "content", true)
                );
            }
        }
    }

}
