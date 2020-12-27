package me.scene.dinner.board.article.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.command.ArticleService;
import me.scene.dinner.board.article.application.command.request.ArticleCreateRequest;
import me.scene.dinner.board.article.application.command.request.ArticleUpdateRequest;
import me.scene.dinner.board.article.application.query.ArticleQueryService;
import me.scene.dinner.board.article.application.query.dto.ArticleView;
import me.scene.dinner.board.article.ui.form.ArticleForm;
import me.scene.dinner.board.article.ui.form.ArticleUpdateForm;
import me.scene.dinner.board.article.ui.form.TagForm;
import me.scene.dinner.common.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;


@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService service;
    private final ArticleQueryService query;

    private final ObjectMapper objectMapper;


    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable Long id,
                              @Principal String username, Model model) {

        service.read(id, username);
        ArticleView article = query.view(id);

        model.addAttribute("article", article);
        return "page/board/article/view";
    }


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
        Set<String> tagNames = parse(form.getJsonTags());

        ArticleCreateRequest request = new ArticleCreateRequest(username, topicId, title, content, publicized, tagNames);
        Long id = service.save(request);

        return "redirect:" + ("/articles/" + id);
    }


    @GetMapping("/articles/{id}/form")
    public String updateForm(@PathVariable Long id, Model model) {

        ArticleView article = query.view(id);

        ArticleUpdateForm form = ArticleUpdateForm.builder()
                .topicId(article.getTopic().getId())
                .title(article.getTitle())
                .content(article.getContent())
                .status(article.isPublicized() ? "PUBLIC" : "PRIVATE")
                .build();

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

        ArticleUpdateRequest request = new ArticleUpdateRequest(username, id, title, content, publicized);
        service.update(request);

        return "redirect:" + ("/articles/" + id);
    }


    @DeleteMapping("/articles/{id}")
    public String delete(@PathVariable Long id,
                         @Principal String username) {

        Long topicId = service.delete(id, username);

        return "redirect:" + ("/topics/" + topicId);
    }


    @GetMapping("/private-articles")
    public String myPrivateArticles() {
        return "page/board/article/private";
    }


    private Set<String> parse(String jsonTags) {
        if (StringUtils.isEmpty(jsonTags)) return new HashSet<>();
        try {
            Set<TagForm> tags = objectMapper.readValue(jsonTags, TagForm.TYPE);
            return tags.stream().map(TagForm::getValue).collect(toSet());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot parse json for tag: " + e.getMessage());
        }
    }

}
