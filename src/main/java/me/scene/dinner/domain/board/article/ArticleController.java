package me.scene.dinner.domain.board.article;

import me.scene.dinner.domain.account.Account;
import me.scene.dinner.domain.account.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ArticleController {

    public static final String URL = "/articles";

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(URL + "/{title}")
    public String showArticle(@PathVariable String title, Model model) {
        // TODO
        model.addAttribute("title", title);
        return "page/board/article/view";
    }

    @PostMapping(URL)
    public String createArticle(@CurrentUser Account current, @Valid ArticleForm articleForm, Errors errors) {
        if (errors.hasErrors()) return "page/board/article/form";

        Long id = articleService.createArticle(current, articleForm);
        return "redirect:" + URL + "/" + id;
    }

}
