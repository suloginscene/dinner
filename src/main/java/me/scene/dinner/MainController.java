package me.scene.dinner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class MainController {

    private final Environment environment;
    private final LocalDateTime startedAt;

    @Autowired
    public MainController(Environment environment) {
        this.environment = environment;
        this.startedAt = LocalDateTime.now();
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("activeProfile", environment.getActiveProfiles()[0]);
        model.addAttribute("version", startedAt);
        return "page/main/home";
    }

    @GetMapping("about")
    public String about() {
        return "page/main/about";
    }

}
