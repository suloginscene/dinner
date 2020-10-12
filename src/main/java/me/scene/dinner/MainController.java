package me.scene.dinner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    public static final String URL_HOME = "/";
    public static final String URL_ABOUT = "/about";

    @GetMapping(URL_HOME)
    public String home() {
        return "page/main/home";
    }

    @GetMapping(URL_ABOUT)
    public String about() {
        return "page/main/about";
    }

}
