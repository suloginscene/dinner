package me.scene.paper.main.ui;

import lombok.RequiredArgsConstructor;
import me.scene.paper.main.application.cache.ExcerptCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final ExcerptCache cache;


    @GetMapping("/")
    public String home(Model model) {
        String randomExcerpt = cache.getRandomExcerpt();

        model.addAttribute("excerpt", randomExcerpt);
        return "page/main/home";
    }

    @GetMapping("about")
    public String about() {
        return "page/main/about";
    }

}
