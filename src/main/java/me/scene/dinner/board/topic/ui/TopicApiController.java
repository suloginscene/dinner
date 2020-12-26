package me.scene.dinner.board.topic.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.topic.application.query.TopicQueryService;
import me.scene.dinner.board.topic.application.query.dto.TopicLink;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class TopicApiController {

    private final TopicQueryService query;


    @GetMapping("/api/topics/of/{magazineId}")
    public List<TopicLink> topicsOfMagazine(@PathVariable Long magazineId) {
        return query.linksOfMagazine(magazineId);
    }

}
