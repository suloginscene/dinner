package me.scene.dinner.board.article.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.article.query.dto.ArticleSimpleDto;
import me.scene.dinner.board.article.query.ArticleQueryService;
import me.scene.dinner.common.security.Current;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleQueryService articleService;

    @GetMapping("/api/articles/{username}")
    public List<ArticleSimpleDto> byUserPublic(@PathVariable String username) {
        return articleService.findPublicByWriter(username);
    }

    @GetMapping("/api/private-articles")
    public List<ArticleSimpleDto> byUserPrivate(@Current Account current) {
        return articleService.findPrivateByWriter(current.getUsername());

    }

}
