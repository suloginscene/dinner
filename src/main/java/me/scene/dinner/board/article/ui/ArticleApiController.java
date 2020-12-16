package me.scene.dinner.board.article.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.article.application.ArticleSimpleDto;
import me.scene.dinner.common.security.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleApiController {

    private final ArticleService articleService;

    @GetMapping("/api/articles/{username}")
    public List<ArticleSimpleDto> byUserPublic(@PathVariable String username) {
        return articleService.findPublicByWriter(username);
    }

    @GetMapping("/api/private-articles")
    public List<ArticleSimpleDto> byUserPrivate(@CurrentUser Account current) {
        return articleService.findPrivateByWriter(current.getUsername());

    }

}
