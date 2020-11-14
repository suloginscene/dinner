package me.scene.dinner.board.article.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.article.domain.Article;
import me.scene.dinner.common.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

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
        articleForm.setTopicId(topicId);
        model.addAttribute("articleForm", articleForm);
        return "page/board/article/form";
    }

    @PostMapping("/articles")
    public String createArticle(@CurrentUser Account current, @Valid ArticleForm form, Errors errors) {
        if (errors.hasErrors()) return "page/board/article/form";

        Long id = articleService.save(form.getTopicId(), current.getUsername(), form.getTitle(), form.getContent());
        return "redirect:" + ("/articles/" + id);
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable Long articleId, @CurrentUser Account current, Model model) {
        Article article = articleService.find(articleId);
        if (!article.isPublished()) {
            article.confirmWriter((current != null) ? current.getUsername() : "anonymousUser");
        }
        model.addAttribute("article", article);
        return "page/board/article/view";
    }

    @PostMapping("/articles/{articleId}")
    public String publish(@PathVariable Long articleId, @CurrentUser Account current) {
        articleService.publish(articleId, (current != null) ? current.getUsername() : "anonymousUser");
        return "redirect:" + ("/articles/" + articleId);
    }

    @GetMapping("/articles/{articleId}/form")
    public String updateForm(@PathVariable Long articleId, @CurrentUser Account current, Model model) {
        Article article = articleService.find(articleId);
        article.confirmWriter(current.getUsername());

        model.addAttribute("id", articleId);
        model.addAttribute("updateForm", updateForm(article));
        return "page/board/article/update";
    }

    private ArticleForm updateForm(Article a) {
        ArticleForm f = new ArticleForm();
        f.setTopicId(a.getTopic().getId());
        f.setTitle(a.getTitle());
        f.setContent(a.getContent());
        return f;
    }

    @PutMapping("/articles/{articleId}")
    public String update(@PathVariable Long articleId, @CurrentUser Account current, @Valid ArticleForm form, Errors errors) {
        // TODO js validation
        if (errors.hasErrors()) return "redirect:" + ("/articles/" + articleId + "/form");

        articleService.update(articleId, current.getUsername(), form.getTitle(), form.getContent());
        return "redirect:" + ("/articles/" + articleId);
    }

}
