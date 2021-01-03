package me.scene.paper;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "page/main/home";
    }

    @GetMapping("about")
    public String about() {
        return "page/main/about";
    }

}
