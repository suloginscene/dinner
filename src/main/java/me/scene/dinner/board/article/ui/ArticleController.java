package me.scene.dinner.board.article.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.article.application.ArticleService;
import me.scene.dinner.board.article.application.ArticleSimpleDto;
import me.scene.dinner.common.security.CurrentUser;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ObjectMapper objectMapper;
    private final ParsedTagObjectSetTypeReference tagSetType;

    @GetMapping("/article-form")
    public String shipArticleForm(@RequestParam Long topicId, Model model) {
        ArticleForm articleForm = new ArticleForm();
        articleForm.setTopicId(topicId);
        model.addAttribute("articleForm", articleForm);
        return "page/board/article/form";
    }

    @PostMapping("/articles")
    public String createArticle(@CurrentUser Account current, @Valid ArticleForm form, Errors errors) {
        if (errors.hasErrors()) return "page/board/article/form";

        Long id = articleService.save(form.getTopicId(), current.getUsername(), form.getTitle(), form.getContent(), form.isPublicized(), parse(form.getJsonTags()));
        return "redirect:" + ("/articles/" + id);
    }

    private Set<String> parse(String jsonTags) {
        if (StringUtils.isEmpty(jsonTags)) return new HashSet<>();
        try {
            Set<ParsedTagObject> tags = objectMapper.readValue(jsonTags, tagSetType);
            return tags.stream().map(ParsedTagObject::getValue).collect(toSet());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot parse json for tag: " + e.getMessage());
        }
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable Long articleId, @CurrentUser Account current, Model model) {
        String username = (current != null) ? current.getUsername() : "anonymousUser";
        ArticleSimpleDto article = articleService.read(articleId, username);
        model.addAttribute("article", article);
        return "page/board/article/view";
    }

    @GetMapping("/articles/{articleId}/form")
    public String updateForm(@PathVariable Long articleId, @CurrentUser Account current, Model model) {
        ArticleSimpleDto article = articleService.findToUpdate(articleId, current.getUsername());
        model.addAttribute("id", articleId);
        model.addAttribute("updateForm", updateForm(article));
        return "page/board/article/update";
    }

    private ArticleForm updateForm(ArticleSimpleDto a) {
        ArticleForm f = new ArticleForm();
        f.setTopicId(a.getTopic().getId());
        f.setTitle(a.getTitle());
        f.setContent(a.getContent());
        f.setStatus(a.getStatus());
        return f;
    }

    @PutMapping("/articles/{articleId}")
    public String update(@PathVariable Long articleId, @CurrentUser Account current, @Valid ArticleForm form, Errors errors) {
        if (errors.hasErrors()) return "redirect:" + ("/articles/" + articleId + "/form");

        articleService.update(articleId, current.getUsername(), form.getTitle(), form.getContent(), form.isPublicized());
        return "redirect:" + ("/articles/" + articleId);
    }

    @DeleteMapping("/articles/{articleId}")
    public String delete(@PathVariable Long articleId, @CurrentUser Account current) {
        Long topicId = articleService.delete(articleId, current.getUsername());
        return "redirect:" + ("/topics/" + topicId);
    }

    @GetMapping("/api/articles/{username}")
    public @ResponseBody
    List<ArticleSimpleDto> byUserPublic(@PathVariable String username) {
        return articleService.findPublicByWriter(username);
    }

    @GetMapping("/private-articles")
    public String byUserPrivate(@CurrentUser Account current, Model model) {
        model.addAttribute("articles", articleService.findPrivateByWriter(current.getUsername()));
        return "page/board/article/private";
    }

}
