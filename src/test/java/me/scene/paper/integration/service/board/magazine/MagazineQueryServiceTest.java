package me.scene.paper.integration.service.board.magazine;

import me.scene.paper.board.common.dto.Link;
import me.scene.paper.board.magazine.application.query.MagazineQueryService;
import me.scene.paper.board.magazine.application.query.dto.MagazineLink;
import me.scene.paper.board.magazine.domain.magazine.model.Magazine;
import me.scene.paper.board.magazine.domain.magazine.repository.MagazineRepository;
import me.scene.paper.board.magazine.domain.magazine.model.Type;
import me.scene.paper.integration.utils.MagazineTestHelper;
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


    @Nested class OnFindListOfUser {
        @Test
        void returns_list() {
            List<MagazineLink> magazines = query.linksOfUser("A");
            assertThat(magazines.size()).isEqualTo(2);
        }
    }

    @Nested class OnFindAll {
        @Test
        void returns_all() {
            List<MagazineLink> magazines = query.allLinks();
            assertThat(magazines.size()).isEqualTo(3);
        }
    }

    @Nested class OnFindBest {
        @Test
        void returns_best() {
            String openMagazine = rate(open, 10);
            String managedMagazine = rate(managed, 20);
            String exclusiveMagazine = rate(exclusive, 30);

            List<MagazineLink> magazines = query.bestLinks(2);
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
