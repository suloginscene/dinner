package me.scene.paper.board.article.ui;

import lombok.RequiredArgsConstructor;
import me.scene.paper.board.article.application.command.ArticleService;
import me.scene.paper.board.article.application.command.request.ArticleCreateRequest;
import me.scene.paper.board.article.application.command.request.ArticleUpdateRequest;
import me.scene.paper.board.article.application.command.request.ReplyCreateRequest;
import me.scene.paper.board.article.application.command.request.ReplyDeleteRequest;
import me.scene.paper.board.article.application.query.ArticleQueryService;
import me.scene.paper.board.article.application.query.dto.ArticleExtendedLink;
import me.scene.paper.board.article.application.query.dto.ArticleView;
import me.scene.paper.board.article.ui.form.ArticleForm;
import me.scene.paper.board.article.ui.form.ArticleUpdateForm;
import me.scene.paper.board.article.ui.form.TagParser;
import me.scene.paper.common.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;


@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService service;
    private final ArticleQueryService query;

    private final TagParser tag;


    @GetMapping("/article-form")
    public String shipArticleForm(@RequestParam Long topicId, Model model) {
        ArticleForm articleForm = new ArticleForm(topicId);

        model.addAttribute("articleForm", articleForm);
        return "page/board/article/form";
    }

    @PostMapping("/articles")
    public String createArticle(@Principal String username,
                                @Valid ArticleForm form, Errors errors) {

        if (errors.hasErrors()) return "page/board/article/form";

        Long topicId = form.getTopicId();
        String title = form.getTitle();
        String content = form.getContent();
        boolean publicized = form.getStatus().equals("PUBLIC");
        Set<String> tagNames = tag.parse(form.getJsonTags());

        ArticleCreateRequest request = new ArticleCreateRequest(username, topicId, title, content, publicized, tagNames);
        Long id = service.save(request);

        return "redirect:" + ("/articles/" + id);
    }


    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable Long id,
                              @Principal String username, Model model) {

        service.read(id, username);
        ArticleView article = query.view(id);

        model.addAttribute("article", article);
        return "page/board/article/view";
    }

    @GetMapping("/drafts")
    public String myPrivateArticles(@Principal String username, Model model) {
        List<ArticleExtendedLink> articles = query.findPrivateByWriter(username);

        model.addAttribute("articles", articles);
        return "page/board/article/private";
    }


    @GetMapping("/articles/{id}/form")
    public String updateForm(@PathVariable Long id, Model model) {

        ArticleView article = query.view(id);

        ArticleUpdateForm form = new ArticleUpdateForm(
                article.getTitle(),
                article.getContent(),
                article.isPublicized() ? "PUBLIC" : "PRIVATE",
                String.join(",", article.getTags())
        );

        model.addAttribute("updateForm", form);
        return "page/board/article/update";
    }

    @PutMapping("/articles/{id}")
    public String update(@Principal String username,
                         @PathVariable Long id,
                         @Valid ArticleUpdateForm form, Errors errors) {

        if (errors.hasErrors()) return "page/board/article/update";

        String title = form.getTitle();
        String content = form.getContent();
        boolean publicized = form.getStatus().equals("PUBLIC");
        Set<String> tagNames = tag.parse(form.getJsonTags());

        ArticleUpdateRequest request = new ArticleUpdateRequest(username, id, title, content, publicized, tagNames);
        service.update(request);

        return "redirect:" + ("/articles/" + id);
    }


    @DeleteMapping("/articles/{id}")
    public String delete(@PathVariable Long id,
                         @Principal String username) {

        Long topicId = service.delete(id, username);

        return "redirect:" + ("/topics/" + topicId);
    }


    @PostMapping("/articles/{id}/replies")
    public String create(@PathVariable Long id,
                         @Principal String username,
                         @RequestParam String content) {

        ReplyCreateRequest request = new ReplyCreateRequest(username, id, content);
        service.save(request);

        return "redirect:" + ("/articles/" + id);
    }

    @DeleteMapping("/articles/{id}/replies/{replyId}")
    public String delete(@PathVariable Long id,
                         @PathVariable Long replyId,
                         @Principal String username) {

        ReplyDeleteRequest request = new ReplyDeleteRequest(username, id, replyId);
        service.delete(request);

        return "redirect:" + ("/articles/" + id);
    }

}
