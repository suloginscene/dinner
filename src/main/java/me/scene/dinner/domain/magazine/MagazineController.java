package me.scene.dinner.domain.magazine;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MagazineController {

    public static final String URL = "/magazines";

    @GetMapping(URL + "/{title}")
    public String readAnArticle(@PathVariable String title, Model model) {
        model.addAttribute("title", title);
        return "page/magazines/view";
    }

}
