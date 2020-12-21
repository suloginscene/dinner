package me.scene.dinner.integration.service;

import me.scene.dinner.board.magazine.domain.common.Type;
import me.scene.dinner.board.topic.application.query.TopicQueryService;
import me.scene.dinner.board.topic.application.query.dto.TopicView;
import me.scene.dinner.integration.utils.MagazineTestHelper;
import me.scene.dinner.integration.utils.TopicTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DisplayName("Topic_query")
class TopicQueryServiceTest {

    @Autowired TopicQueryService service;

    @Autowired TopicTestHelper helper;
    @Autowired MagazineTestHelper magazineHelper;


    Long id;

    @BeforeEach
    void create() {
        Long magazineId = magazineHelper.createMagazine("magazineOwner", "magazine", Type.OPEN);
        id = helper.createTopic("user", magazineId, "topic");
    }

    @AfterEach
    void clear() {
        helper.clearTopics();
        magazineHelper.clearMagazines();
    }


    @Nested class OnFindView {
        @Test
        void load_magazine() {
            TopicView topic = service.find(id);
            assertThat(topic.getMagazine()).isNotNull();
        }
    }

}
