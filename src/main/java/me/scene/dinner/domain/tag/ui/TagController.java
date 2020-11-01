package me.scene.dinner.domain.tag.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TagController {

    public static final String URL = "/tags";

    @GetMapping(URL)
    public String showTags() {
        return "page/tag/list";
    }

    @GetMapping(URL + "/{title}")
    public String showTag(@PathVariable String title, Model model) {
        model.addAttribute("title", title);
        return "page/tag/view";
    }

}
