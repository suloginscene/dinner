package me.scene.paper.service.home.ui;

import lombok.RequiredArgsConstructor;
import me.scene.paper.service.home.application.cache.ExcerptCache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class HomeController {

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
