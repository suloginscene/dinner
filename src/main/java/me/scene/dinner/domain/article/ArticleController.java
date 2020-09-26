package me.scene.dinner.domain.article;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ArticleController {

    public static final String URL = "/articles";

    @GetMapping(URL + "/{title}")
    public String readAnArticle(@PathVariable String title, Model model) {
        model.addAttribute("title", title);
        return "page/article/read";
    }

}
