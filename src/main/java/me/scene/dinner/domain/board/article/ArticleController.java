package me.scene.dinner.domain.board.article;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {

    public static final String FORM = "/article-form";
    public static final String URL = "/articles";

    @GetMapping(FORM)
    public String shipArticleForm(Model model) {
        model.addAttribute(new ArticleForm());
        return "page/board/article/form";
    }

    @GetMapping(URL + "/{title}")
    public String showArticle(@PathVariable String title, Model model) {
        // TODO
        model.addAttribute("title", title);
        return "page/board/article/view";
    }

    @PostMapping(URL)
    public String createArticle(ArticleForm articleForm) {
        System.out.println(articleForm.getContent());
        // TODO
        String title = null;
        return "redirect:" + URL + "/" + title;
    }

}
