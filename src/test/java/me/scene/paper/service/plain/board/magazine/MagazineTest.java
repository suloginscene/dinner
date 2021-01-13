package me.scene.paper.service.plain.board.magazine;

import me.scene.paper.service.board.common.domain.exception.NotDeletableException;
import me.scene.paper.service.board.common.domain.exception.NotOwnerException;
import me.scene.paper.service.board.magazine.domain.magazine.model.Magazine;
import me.scene.paper.service.board.magazine.domain.magazine.model.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("Magazine")
class MagazineTest {

    Magazine magazine;

    @BeforeEach
    void create() {
        magazine = Magazine.create(Type.OPEN, "owner", "magazine", "short", "long");
    }


    @Nested class OnCreate {
        @Test
        void has_properties() {

            assertThat(magazine.getPoint()).isNotNull();
            assertThat(magazine.getTopics()).isNotNull();
        }
    }

    @Nested class OnUpdate {
        @Test
        void updates() {
            magazine.update("owner", "updated", "short", "long");
            assertThat(magazine.getTitle()).isEqualTo("updated");
        }

        @Nested class WithNotOwner {
            @Test
            void throws_exception() {
                assertThrows(NotOwnerException.class,
                        () -> magazine.update("notOwner", "updated", "short", "long"));
            }
        }
    }

    @Nested class OnDelete {
        @Test
        void deletes() {
            magazine.beforeDelete("owner");
        }

        @Nested class OnHasChild {
            @Test
            void throws_exception() {
                magazine.getTopics().add();
                assertThrows(NotDeletableException.class,
                        () -> magazine.beforeDelete("owner"));
            }
        }

        @Nested class WithNotOwner {
            @Test
            void throws_exception() {
                assertThrows(NotOwnerException.class,
                        () -> magazine.beforeDelete("notOwner"));
            }
        }
    }

}
