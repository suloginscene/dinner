package me.scene.dinner.board.article.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.command.ArticleService;
import me.scene.dinner.board.article.application.query.ArticleQueryService;
import me.scene.dinner.board.article.application.query.dto.ArticleExtendedLink;
import me.scene.dinner.common.security.Principal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleService service;
    private final ArticleQueryService query;


    @GetMapping("/api/articles")
    public List<ArticleExtendedLink> byUserPublic(@RequestParam String username) {
        return query.findPublicByWriter(username);
    }

    @GetMapping("/api/topics/{id}/articles")
    public List<ArticleExtendedLink> articlesOfTopic(@PathVariable Long id) {
        return query.linksOfTopic(id);
    }


    @GetMapping("/api/articles/{id}/like")
    boolean doesLike(@PathVariable Long id, @Principal String username) {
        return query.doesLike(username, id);
    }

    @PostMapping("/api/articles/{id}/like")
    void likes(@PathVariable Long id, @Principal String username) {
        service.like(username, id);
    }

    @DeleteMapping("/api/articles/{id}/like")
    void dislike(@PathVariable Long id, @Principal String username) {
        service.dislike(username, id);
    }

}
