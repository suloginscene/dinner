package me.scene.dinner.domain;

import me.scene.dinner.infra.environment.ActiveProfile;
import me.scene.dinner.infra.environment.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    public static final String URL_HOME = "/";
    public static final String URL_ABOUT = "/about";

    private final ActiveProfile activeProfile;
    private final Version version;

    @Autowired
    public MainController(ActiveProfile activeProfile, Version version) {
        this.activeProfile = activeProfile;
        this.version = version;
    }

    @GetMapping(URL_HOME)
    public String home(Model model) {
        model.addAttribute("activeProfile", activeProfile.get());
        model.addAttribute("version", version.get());
        return "page/main/home";
    }

    @GetMapping(URL_ABOUT)
    public String about() {
        return "page/main/about";
    }

}
