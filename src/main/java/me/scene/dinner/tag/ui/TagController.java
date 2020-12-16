package me.scene.dinner.tag.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.tag.application.dto.TagDto;
import me.scene.dinner.tag.application.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TagController {

    private final TagService service;

    @GetMapping("/tags")
    public String showTags(Model model) {
        List<String> tags = service.findAllTagNames();
        model.addAttribute("tags", tags);
        return "page/tag/list";
    }

    @GetMapping("/tags/{name}")
    public String showTaggedArticles(@PathVariable String name, Model model) {
        TagDto tag = service.findLoadedTag(name);
        model.addAttribute("tag", tag);
        return "page/tag/view";
    }

}
