package me.scene.dinner.tag;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TaggedArticleController {

    @GetMapping("/tags/{name}")
    public String showTag(@PathVariable String name, Model model) {
        model.addAttribute("name", name);
        return "page/tag/view";
    }

}
