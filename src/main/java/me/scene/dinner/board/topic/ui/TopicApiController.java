package me.scene.dinner.board.topic.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.model.Account;
import me.scene.dinner.board.article.application.query.dto.ArticleExtendedLink;
import me.scene.dinner.board.topic.application.query.TopicQueryService;
import me.scene.dinner.board.topic.application.query.dto.TopicLink;
import me.scene.dinner.common.security.Current;
import org.springframework.http.ResponseEntity;
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
        return query.findTopics(magazineId);
    }

}
