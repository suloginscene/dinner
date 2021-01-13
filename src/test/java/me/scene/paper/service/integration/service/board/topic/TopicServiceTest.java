package me.scene.paper.service.integration.service.board.topic;

import me.scene.paper.service.board.magazine.domain.magazine.model.Type;
import me.scene.paper.service.board.topic.application.command.TopicService;
import me.scene.paper.service.integration.utils.MagazineTestHelper;
import me.scene.paper.service.integration.utils.TopicTestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@DisplayName("Topic_service")
class TopicServiceTest {

    @Autowired TopicService service;

    @Autowired TopicTestHelper helper;
    @Autowired MagazineTestHelper magazineHelper;


    Long magazineId;

    @BeforeEach
    void create() {
        magazineId = magazineHelper.createMagazine("magazineOwner", "magazine", Type.OPEN);
    }

    @AfterEach
    void clear() {
        helper.clearTopics();
        magazineHelper.clearMagazines();
    }

}
