package me.scene.dinner.domain.board;

import me.scene.dinner.domain.account.Account;
import me.scene.dinner.domain.account.CurrentUser;
import me.scene.dinner.domain.board.article.ArticleForm;
import me.scene.dinner.domain.board.article.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class BoardController {

    private static final String FORM = "/board-form";
    private static final String BOARD = "/board";

    private static final String MAGAZINE_FORM = FORM;
    private static final String TOPIC_FORM = FORM + "/{magazine}";
    private static final String ARTICLE_FORM = FORM + "/{magazine}" + "/{topic}";

    private static final String MAGAZINE_POST = BOARD;
    private static final String TOPIC_POST = BOARD + "/{magazine}";
    private static final String ARTICLE_POST = BOARD + "/{magazine}" + "/{topic}";

    private static final String MAGAZINE_READ = BOARD + "/{magazine}";
    private static final String TOPIC_READ = BOARD + "/{magazine}" + "/{topic}";
    private static final String ARTICLE_READ = BOARD + "/{magazine}" + "/{topic}" + "/{article}";

    private final ArticleService articleService;

    @Autowired
    public BoardController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @GetMapping(MAGAZINE_FORM)
    public String shipMagazineForm() {
        return "page/board/magazine/form";
    }

    @GetMapping(MAGAZINE_READ)
    public String showMagazine(@PathVariable String magazine, Model model) {
        model.addAttribute("title", magazine);
        return "page/board/magazine/view";
    }


    @GetMapping(TOPIC_FORM)
    public String shipTopicForm(@PathVariable String magazine) {
        return "page/board/topic/form";
    }

    @GetMapping(TOPIC_READ)
    public String showTopic(@PathVariable String magazine, @PathVariable String topic, Model model) {
        model.addAttribute("title", topic);
        return "page/board/topic/view";
    }


    @GetMapping(ARTICLE_FORM)
    public String shipArticleForm(@PathVariable String magazine, @PathVariable String topic, Model model) {
        ArticleForm articleForm = new ArticleForm();
        model.addAttribute(articleForm);
        return "page/board/article/form";
    }

    @GetMapping(ARTICLE_READ)
    public String showArticle(@PathVariable String magazine, @PathVariable String topic, @PathVariable String article, Model model) {
        // TODO
        model.addAttribute("title", article);
        return "page/board/article/view";
    }

    @PostMapping(ARTICLE_POST)
    public String createArticle(@PathVariable String magazine, @PathVariable String topic, @CurrentUser Account current, @Valid ArticleForm articleForm, Errors errors) {
        if (errors.hasErrors()) return "page/board/article/form";

        String title = articleService.createArticle(current, topic, articleForm);
        String path = "/board" + "/" + magazine + "/" + topic + "/" + title;
        return "redirect:" + path;
    }

}
