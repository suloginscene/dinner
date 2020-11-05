package me.scene.dinner.board.article.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.board.article.application.ArticleDto;
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
        ArticleForm articleForm = new ArticleForm();
        model.addAttribute("topicId", topicId);
        model.addAttribute("articleForm", articleForm);
        return "page/board/article/form";
    }

    @PostMapping("/articles")
    public String createArticle(@RequestParam Long topicId, @CurrentUser Account current,
                                @Valid ArticleForm form, Errors errors) {
        if (errors.hasErrors()) return "page/board/article/form";

        Long id = articleService.save(topicId, current.getId(),
                form.getTitle(), form.getContent());
        return "redirect:" + ("/articles/" + id);
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable Long articleId, Model model) {
        ArticleDto articleDto = articleService.extractDto(articleId);
        model.addAttribute("articleDto", articleDto);
        return "page/board/article/view";
    }

}
