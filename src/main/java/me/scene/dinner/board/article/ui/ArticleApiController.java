package me.scene.dinner.board.article.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.query.ArticleQueryService;
import me.scene.dinner.board.article.application.query.dto.ArticleExtendedLink;
import me.scene.dinner.common.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleQueryService query;


    @GetMapping("/api/articles/{username}")
    public List<ArticleExtendedLink> byUserPublic(@PathVariable String username) {
        return query.findPublicByWriter(username);
    }

    @GetMapping("/api/private-articles")
    public List<ArticleExtendedLink> byUserPrivate(@Principal String username) {
        return query.findPrivateByWriter(username);
    }

    @GetMapping("/api/articles/of/{topicId}")
    public List<ArticleExtendedLink> articlesOfTopic(@PathVariable Long topicId) {
        return query.linksOfTopic(topicId);
    }

}
