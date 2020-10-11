package me.scene.dinner.domain.board.article;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ArticleController {

    public static final String FORM = "/article-form";
    public static final String URL = "/articles";

    @GetMapping(FORM)
    public String shipArticleForm() {
        return "page/board/article/form";
    }

    @GetMapping(URL + "/{title}")
    public String showArticle(@PathVariable String title, Model model) {
        model.addAttribute("title", title);
        return "page/board/article/view";
    }

}
