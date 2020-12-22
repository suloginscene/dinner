package me.scene.dinner.board.article.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.model.Account;
import me.scene.dinner.board.article.application.query.ArticleQueryService;
import me.scene.dinner.board.article.application.query.dto.ArticleExtendedLink;
import me.scene.dinner.common.security.Current;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleQueryService service;


    @GetMapping("/api/articles/{username}")
    public ResponseEntity<List<ArticleExtendedLink>> byUserPublic(@PathVariable String username) {
        List<ArticleExtendedLink> articles = service.findPublicByWriter(username);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/api/private-articles")
    public ResponseEntity<List<ArticleExtendedLink>> byUserPrivate(@Current Account current) {
        List<ArticleExtendedLink> articles = service.findPrivateByWriter(current.getUsername());
        return ResponseEntity.ok(articles);
    }

}
