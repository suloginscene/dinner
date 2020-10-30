package me.scene.dinner.domain.board;

import me.scene.dinner.domain.account.Account;
import me.scene.dinner.domain.account.AccountService;
import me.scene.dinner.domain.account.CurrentUser;
import me.scene.dinner.domain.board.article.Article;
import me.scene.dinner.domain.board.article.ArticleForm;
import me.scene.dinner.domain.board.article.ArticleService;
import me.scene.dinner.infra.util.DateUtils;
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

    private final AccountService accountService;
    private final ArticleService articleService;

    @Autowired
    public BoardController(AccountService accountService, ArticleService articleService) {
        this.accountService = accountService;
        this.articleService = articleService;
    }

    // TODO articleForm binder to validate url in topic

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
        model.addAttribute("magazine", magazine);
        model.addAttribute("topic", topic);
        model.addAttribute("articleForm", new ArticleForm());
        return "page/board/article/form";
    }

    @PostMapping(ARTICLE_POST)
    public String createArticle(@PathVariable String magazine, @PathVariable String topic, @CurrentUser Account current, @Valid ArticleForm articleForm, Errors errors) {
        if (errors.hasErrors()) return "page/board/article/form";

        String url = articleService.createArticle(current, topic, articleForm);
        String path = "/board" + "/" + magazine + "/" + topic + "/" + url;
        return "redirect:" + path;
    }

    @GetMapping(ARTICLE_READ)
    public String showArticle(@PathVariable String magazine, @PathVariable String topic, @PathVariable String article, Model model) {
        model.addAttribute("magazine", magazine);
        model.addAttribute("topic", topic);
        Article comp = articleService.findByUrl(article);
        model.addAttribute("writer", accountService.findUsernameById(comp.getWriter()));
        model.addAttribute("title", comp.getTitle());
        model.addAttribute("content", comp.getContent());
        model.addAttribute("date", DateUtils.format(comp.getDate()));
        return "page/board/article/view";
    }

}
