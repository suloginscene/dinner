package me.scene.dinner.domain.board.topic;

import me.scene.dinner.domain.board.article.ArticleForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TopicController {

    public static final String URL = "/topics";

    public static final String ARTICLE_FORM = "/article-form";

    @GetMapping(ARTICLE_FORM)
    public String shipArticleForm(Model model) {
        // TODO
        Topic topic = new Topic();
        ArticleForm articleForm = new ArticleForm();
        articleForm.setTopic(topic);
        model.addAttribute(articleForm);
        return "page/board/article/form";
    }

    @GetMapping(URL + "/{title}")
    public String showTopic(@PathVariable String title, Model model) {
        model.addAttribute("title", title);
        return "page/board/topic/view";
    }

}
