package me.scene.paper.service.board.topic.ui;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.board.topic.application.query.TopicQueryService;
import me.scene.paper.service.board.topic.application.query.dto.TopicLink;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class TopicApiController {

    private final TopicQueryService query;


    @GetMapping("/api/magazines/{id}/topics")
    public List<TopicLink> topicsOfMagazine(@PathVariable Long id) {
        return query.linksOfMagazine(id);
    }

}
