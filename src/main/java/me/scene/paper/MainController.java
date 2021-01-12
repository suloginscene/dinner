package me.scene.paper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final Excerpts excerpts;


    @GetMapping("/")
    public String home(Model model) {
        String randomExcerpt = excerpts.getRandomExcerpt();

        model.addAttribute("excerpt", randomExcerpt);
        return "page/main/home";
    }

    @GetMapping("about")
    public String about() {
        return "page/main/about";
    }


    @Component
    public static class Excerpts {

        private final List<String> excerpts = new ArrayList<>();
        private final Random random = new Random();


        public List<String> getExcerpts() {
            return excerpts;
        }

        public String getRandomExcerpt() {
            if (excerpts.isEmpty()) return "페이퍼에 오신 것을 환영합니다.";

            int i = random.nextInt(excerpts.size());
            return excerpts.get(i);
        }

        public void add(String excerpt) {
            excerpts.add(excerpt);
        }

        public void remove(String excerpt) {
            excerpts.remove(excerpt);
        }

    }

}
