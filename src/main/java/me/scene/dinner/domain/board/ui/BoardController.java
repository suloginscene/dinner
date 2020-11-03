package me.scene.dinner.domain.board.ui;

import me.scene.dinner.domain.account.domain.Account;
import me.scene.dinner.domain.board.application.ArticleService;
import me.scene.dinner.infra.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class BoardController {

    private final ArticleService articleService;

    private final ArticleFormValidator articleFormValidator;

    @Autowired
    public BoardController(ArticleService articleService, ArticleFormValidator articleFormValidator) {
        this.articleService = articleService;
        this.articleFormValidator = articleFormValidator;
    }


    @InitBinder("articleForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(articleFormValidator);
    }


    @GetMapping("/magazines/{magazineId}/topic-form")
    public String shipTopicForm(@PathVariable String magazine) {
        return "page/board/topic/form";
    }


    @GetMapping("/topics/{topicId}")
    public String showTopic(@PathVariable String magazine, @PathVariable String topic, Model model) {
        model.addAttribute("title", topic);
        return "page/board/topic/view";
    }

    @GetMapping("/topics/{topicId}/article-form")
    public String shipArticleForm(@PathVariable String magazine, @PathVariable String topic, Model model) {
        model.addAttribute("magazine", magazine);
        model.addAttribute("topic", topic);
        ArticleForm articleForm = new ArticleForm();
        articleForm.setParentUrl("/" + magazine + "/" + topic + "/");
        model.addAttribute("articleForm", articleForm);
        return "page/board/article/form";
    }


    @PostMapping("/articles")
    public String createArticle(@PathVariable String magazine, @PathVariable String topic,
                                @CurrentUser Account current, @Valid ArticleForm articleForm, Errors errors) {
        if (errors.hasErrors()) return "page/board/article/form";

        String url = articleService.createArticle(current, topic, articleForm);
        String path = "/board" + url;
        return "redirect:" + path;
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable String magazine, @PathVariable String topic, @PathVariable String article, Model model) {
        model.addAttribute("magazine", magazine);
        model.addAttribute("topic", topic);
        String url = "/" + magazine + "/" + topic + "/" + article;
//        Article comp = articleService.findByUrl(url);
//        model.addAttribute("writer", accountService.findUsernameById(comp.getWriter()));
//        model.addAttribute("title", comp.getTitle());
//        model.addAttribute("content", comp.getContent());
//        model.addAttribute("date", DateUtils.format(comp.getDate()));
        return "page/board/article/view";
    }

}
