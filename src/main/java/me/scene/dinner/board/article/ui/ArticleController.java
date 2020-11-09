package me.scene.dinner.board.article.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.common.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping("/article-form")
    public String shipArticleForm(@RequestParam Long topicId, Model model) {
        model.addAttribute("topicId", topicId);
        model.addAttribute("articleForm", new ArticleForm());
        return "page/board/article/form";
    }

    @PostMapping("/articles")
    public String createArticle(@RequestParam Long topicId, @CurrentUser Account current,
                                @Valid ArticleForm form, Errors errors) {
        if (errors.hasErrors()) return "page/board/article/form";

        Long id = articleService.save(topicId, current.getUsername(), form.getTitle(), form.getContent());
        return "redirect:" + ("/articles/" + id);
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable Long articleId, @CurrentUser Account current, Model model) {
        String username = (current != null) ? current.getUsername() : "anonymousUser";
        model.addAttribute("article", articleService.find(articleId, username));
        return "page/board/article/view";
    }

    @PostMapping("/articles/{articleId}")
    public String publish(@PathVariable Long articleId, @CurrentUser Account current) {
        String username = (current != null) ? current.getUsername() : "anonymousUser";
        articleService.publish(articleId, username);
        return "redirect:" + ("/articles/" + articleId);
    }

}
