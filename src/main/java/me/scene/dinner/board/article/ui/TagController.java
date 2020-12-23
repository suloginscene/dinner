package me.scene.dinner.board.article.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.board.article.application.query.TagQueryService;
import me.scene.dinner.board.article.application.query.dto.TagView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class TagController {

    private final TagQueryService query;


    @GetMapping("/tags")
    public String showTags(Model model) {
        List<String> tags = query.findAllTagNames();

        model.addAttribute("tags", tags);
        return "page/board/article/tags";
    }

    @GetMapping("/tags/{name}")
    public String showTaggedArticles(@PathVariable String name, Model model) {
        TagView tag = query.find(name);

        model.addAttribute("tag", tag);
        return "page/board/article/tag";
    }

}
