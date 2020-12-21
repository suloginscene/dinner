package me.scene.dinner.integration.service;

import me.scene.dinner.board.common.dto.Link;
import me.scene.dinner.board.magazine.application.query.MagazineQueryService;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import me.scene.dinner.board.magazine.application.query.dto.MagazineView;
import me.scene.dinner.board.magazine.domain.common.Magazine;
import me.scene.dinner.board.magazine.domain.common.MagazineRepository;
import me.scene.dinner.board.magazine.domain.common.Type;
import me.scene.dinner.integration.utils.MagazineTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DisplayName("Magazine_query")
class MagazineQueryServiceTest {

    @Autowired MagazineQueryService query;

    @Autowired MagazineRepository repository;

    @Autowired MagazineTestHelper helper;


    Long open;
    Long managed;
    Long exclusive;

    @BeforeEach
    void create() {
        open = helper.createMagazine("A", "open1", Type.OPEN);
        managed = helper.createMagazine("A", "managed1", Type.MANAGED);
        exclusive = helper.createMagazine("B", "exclusive1", Type.EXCLUSIVE);
    }

    @AfterEach
    void clear() {
        helper.clearMagazines();
    }


    @Nested class OnFindView {
        @Nested class WithOpen {
            @Test
            void has_writers() {
                MagazineView magazine = query.find(open);
                assertThat(magazine.getWriters()).isNotNull();
                assertThat(magazine.getMembers()).isNull();
            }
        }

        @Nested class WithManaged {
            @Test
            void has_members() {
                MagazineView magazine = query.find(managed);
                assertThat(magazine.getWriters()).isNull();
                assertThat(magazine.getMembers()).isNotNull();
            }
        }

        @Nested class WithExclusive {
            @Test
            void does_not_have() {
                MagazineView magazine = query.find(exclusive);
                assertThat(magazine.getWriters()).isNull();
                assertThat(magazine.getMembers()).isNull();
            }
        }
    }

    @Nested class OnFindListOfUser {
        @Test
        void returns_list() {
            List<MagazineLink> magazines = query.findByUsername("A");
            assertThat(magazines.size()).isEqualTo(2);
        }
    }

    @Nested class OnFindAll {
        @Test
        void returns_all() {
            List<MagazineLink> magazines = query.findAll();
            assertThat(magazines.size()).isEqualTo(3);
        }
    }

    @Nested class OnFindBest {
        @Test
        void returns_best() {
            String openMagazine = rate(open, 10);
            String managedMagazine = rate(managed, 20);
            String exclusiveMagazine = rate(exclusive, 30);

            List<MagazineLink> magazines = query.findBest(2);
            assertThat(magazines.size()).isEqualTo(2);

            List<String> titles = magazines.stream().map(Link::getTitle).collect(Collectors.toList());
            assertThat(titles).doesNotContain(openMagazine);
            assertThat(titles).contains(managedMagazine);
            assertThat(titles).contains(exclusiveMagazine);
        }

        private String rate(Long id, int point) {
            Magazine magazine = repository.find(id);
            magazine.rate(point);
            repository.save(magazine);
            return magazine.getTitle();
        }
    }

}
