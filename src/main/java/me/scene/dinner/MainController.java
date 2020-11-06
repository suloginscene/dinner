package me.scene.dinner;

import me.scene.dinner.board.magazine.application.MagazineDto;
import me.scene.dinner.board.magazine.application.MagazineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class MainController {

    private final Environment environment;
    private final LocalDateTime startedAt;

    private final MagazineService magazineService;

    @Autowired
    public MainController(Environment environment, MagazineService magazineService) {
        this.environment = environment;
        this.startedAt = LocalDateTime.now();
        this.magazineService = magazineService;
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("activeProfile", environment.getActiveProfiles()[0]);
        model.addAttribute("version", DateTimeFormatter.ISO_DATE_TIME.format(startedAt));

        // TODO AJAX / semi batch
        List<MagazineDto> magazineDtoList = magazineService.findAllAsDto();
        model.addAttribute("magazineDtoList", magazineDtoList);

        return "page/main/home";
    }

    @GetMapping("about")
    public String about() {
        return "page/main/about";
    }

}
